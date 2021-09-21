package it.trvi.simpleicd10;
import it.trvi.easyxml.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

/**
 * An object that contains the whole ICD-10-CM classification and that can thus provide information about the whole classification or about single codes.
 */
public class ICD10CMCodesManipulator {

    private ArrayList<ICDNode> chapterList;
    private HashMap<String, ICDNode> codeToNode;
    private ArrayList<String> allCodesList;
    private ArrayList<String> allCodesListNoDots;
    private HashMap<String, Integer> codeToIndexMap;

    public static void main(String[] args) throws IOException, ParseException {
        ICD10CMCodesManipulator c = new ICD10CMCodesManipulator();
    }

    /**
     * The constructor that reads the data from the xml file to load all the data relative to the ICD-10-CM classification.
     */
    public ICD10CMCodesManipulator(){
        //loads the list of all codes, to remove later from the tree the ones that do not exist for very specific rules not easily extracted from the XML file
        InputStream in = getClass().getResourceAsStream("icd10cm-order-Jan-2021.txt");
        Set<String> validCodesSet = new HashSet<>();
        try {
            String fileContent = new String(in.readAllBytes());
            String[] linesOfFile = fileContent.split("\n");
            for(String line: linesOfFile){
                validCodesSet.add(line.substring(6,13).strip());
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("Error while trying to read the list of valid codes",e);
        }

        //opens the XML file
        XMLElement xmlData;
        try {
            xmlData = XMLTreeBuilder.buildFromStream(getClass().getResourceAsStream("icd10cm_tabular_2021.xml"));
        } catch (Exception e) {
            throw new RuntimeException("Error while trying to read the XML file containing the ICD-10-CM classification",e);
        }
        xmlData.removeChild(1);
        xmlData.removeChild(0);

        //creates the tree
        this.chapterList = new ArrayList<>();
        this.codeToNode = new HashMap<>();
        this.codeToIndexMap = new HashMap<>();
        for(XMLElement child: xmlData.getAllChildren()){
            this.chapterList.add(new ICDNode(child,null,null,null,null,null,validCodesSet));
        }

        //initializes the two lists of codes
        this.allCodesList = new ArrayList<>();
        this.allCodesListNoDots = new ArrayList<>();
        for(ICDNode chapter: this.chapterList){
            addTreeToList(chapter);
        }
    }

    /**
     * Private class that represents a single node of the tree of ICD-10 codes.
     */
    private class ICDNode {
        private String name;
        private String description;
        private String type;
        private ICDNode parent;
        private ArrayList<ICDNode> children;
        private ArrayList<String> excludes1;
        private ArrayList<String> excludes2;
        private ArrayList<String> includes;
        private ArrayList<String> inclusionTerm;
        private HashMap<String, String> sevenChrDef;
        private ICDNode sevenChrDefAncestor;
        private String sevenChrNote;
        private ICDNode sevenChrNoteAncestor;
        private String useAdditionalCode;
        private ICDNode useAdditionalCodeAncestor;
        private String codeFirst;
        private ICDNode codeFirstAncestor;

        private ICDNode(XMLElement tree, ICDNode parent, ICDNode sevenChrDefAncestor, ICDNode sevenChrNoteAncestor, ICDNode useAdditionalCodeAncestor, ICDNode codeFirstAncestor, Set<String> validCodesSet) {
            this.children = new ArrayList<>();
            this.parent = parent;
            this.sevenChrDefAncestor = sevenChrDefAncestor;
            this.sevenChrNoteAncestor = sevenChrNoteAncestor;
            this.useAdditionalCodeAncestor = useAdditionalCodeAncestor;
            this.codeFirstAncestor = codeFirstAncestor;
            this.sevenChrNote = "";
            this.useAdditionalCode = "";
            this.codeFirst = "";

            //reads all the data from the XMLElement and creates children nodes
            ICDNode newSevenChrDefAncestor = sevenChrDefAncestor;
            ICDNode newSevenChrNoteAncestor = sevenChrNoteAncestor;
            ICDNode newUseAdditionalCodeAncestor = useAdditionalCodeAncestor;
            ICDNode newCodeFirstAncestor = codeFirstAncestor;
            if(tree.hasAttribute("id")){//the name of sections is an attribute instead of text inside an XML element
                this.name=tree.getAttribute("id");
            }
            for(XMLElement subtree: tree.getAllChildren()){
                switch (subtree.getTagName()){
                    case "diag":
                    case "section":
                        this.children.add(new ICDNode(subtree,this,newSevenChrDefAncestor,newSevenChrNoteAncestor,newUseAdditionalCodeAncestor,newCodeFirstAncestor,validCodesSet));
                        break;

                    case "name":
                        this.name=subtree.getTextContent();
                        break;

                    case "desc":
                        this.description=subtree.getTextContent();
                        break;

                    case "excludes1":
                        if(this.excludes1==null){
                            this.excludes1=new ArrayList<>();
                        }
                        for(XMLElement note: subtree.getAllChildren()){
                            this.excludes1.add(note.getTextContent());
                        }

                    case "excludes2":
                        if(this.excludes2==null){
                            this.excludes2=new ArrayList<>();
                        }
                        for(XMLElement note: subtree.getAllChildren()){
                            this.excludes2.add(note.getTextContent());
                        }
                        break;

                    case "includes":
                        if(this.includes==null){
                            this.includes=new ArrayList<>();
                        }
                        for(XMLElement note: subtree.getAllChildren()){
                            this.includes.add(note.getTextContent());
                        }
                        break;

                    case "inclusionTerm":
                        if(this.inclusionTerm==null){
                            this.inclusionTerm=new ArrayList<>();
                        }
                        for(XMLElement note: subtree.getAllChildren()){
                            this.inclusionTerm.add(note.getTextContent());
                        }
                        break;

                    case "sevenChrDef":
                        if(this.sevenChrDef==null){
                            this.sevenChrDef=new HashMap<>();
                        }
                        String lastChar = "_error_";
                        for(XMLElement extension: subtree.getAllChildren()){
                            if(extension.getTagName().equals("extension")){
                                this.sevenChrDef.put(extension.getAttribute("char"),extension.getTextContent());
                            }else if(extension.getTagName().equals("note")){
                                this.sevenChrDef.put(lastChar,this.sevenChrDef.get(lastChar)+"/"+extension.getTextContent());
                            }
                        }
                        newSevenChrDefAncestor=this;
                        break;

                    case "sevenChrNote":
                        this.sevenChrNote=subtree.getChildAt(0).getTextContent();
                        newSevenChrNoteAncestor=this;
                        break;

                    case "useAdditionalCode"://in case there are multiple lines
                        for(XMLElement child: subtree.getAllChildren()){
                            this.useAdditionalCode=this.useAdditionalCode+"\n"+child.getTextContent();
                        }
                        newUseAdditionalCodeAncestor=this;
                        break;

                    case "codeFirst"://in case there are multiple lines
                        for(XMLElement child: subtree.getAllChildren()){
                            this.codeFirst=this.codeFirst+"\n"+child.getTextContent();
                        }
                        newCodeFirstAncestor=this;
                        break;
                }

            }

            //cleans the useAdditionalCode and codeFirst attributes from extra new lines
            if (this.useAdditionalCode.length()>0 && this.useAdditionalCode.charAt(0)=='\n'){
                this.useAdditionalCode=this.useAdditionalCode.substring(1);
            }
            if (this.codeFirst.length()>0 && this.codeFirst.charAt(0)=='\n'){
                this.codeFirst=this.codeFirst.substring(1);
            }

            //sets the type
            switch (tree.getTagName()){
                case "chapter":
                    this.type="chapter";
                    break;

                case "section":
                    this.type="section";
                    break;

                case "diag_ext":
                    this.type="extended subcategory";
                    break;

                case "diag":
                    if(tree.getTagName().length()==3){
                        this.type="category";
                    }else{
                        this.type="subcategory";
                    }
            }

            //adds node to codeToNode HashMap
            if(!codeToNode.containsKey(this.name)){//in case a section has the same name of a code (ex B99)
                codeToNode.put(this.name, this);
            }

            //if this code is a leaf, it adds to its children the codes created by adding the seventh character
            if(this.children.size()==0 && (this.sevenChrDef!=null || this.sevenChrDefAncestor!=null) && !this.type.equals("extended subcategory")){
                HashMap<String,String> map;
                if(sevenChrDef!=null){
                    map=this.sevenChrDef;
                }else{
                    map=this.sevenChrDefAncestor.sevenChrDef;
                }
                StringBuilder extendedName = new StringBuilder(this.name);
                if(extendedName.length()==3){
                    extendedName.append(".");
                }
                while (extendedName.length()<7){//adds the placeholder X if needed
                    extendedName.append("X");
                }
                for(Map.Entry<String, String> entry : map.entrySet()){
                    if(validCodesSet.contains(extendedName.substring(0,3)+extendedName.substring(4)+entry.getKey())){
                        try {
                            this.children.add(new ICDNode(XMLTreeBuilder.buildFromString("<diag_ext><name>"+extendedName.toString()+entry.getKey()+"</name><desc>"+this.description+", "+entry.getValue()+"</desc></diag_ext>"),this,newSevenChrDefAncestor,newSevenChrNoteAncestor,newUseAdditionalCodeAncestor,newCodeFirstAncestor,validCodesSet));
                        } catch (ParseException e) {
                            throw new RuntimeException("Error while creating the extended subcategories",e);
                        }
                    }
                }
            }

        }
    }

    /**
     * Private methods that takes an ICDNode and adds it and its children (their String representation) to allCodesList and allCodesListNoDots, with a depth-first pre-order visit.
     */
    private void addTreeToList(ICDNode node){
        String name = node.name;
        this.allCodesList.add(name);
        if(name.length()>4 && name.charAt(3)=='.'){
            allCodesListNoDots.add(name.substring(0,3)+name.substring(4));
        }else{
            allCodesListNoDots.add(name);
        }
        for(ICDNode child: node.children){
            addTreeToList(child);
        }
    }

    /**
     * Private method used to add the dot to a code without having to check whether the code is valid.
     */
    private String addDotToCode(String code){
        if(code.length()<4 || code.charAt(3)=='.'){
            return code;
        } else if(codeToNode.containsKey(code.substring(0,3)+"."+code.substring(3))){
            return code.substring(0,3)+"."+code.substring(3);
        } else {
            return code;
        }
    }

    /**
     * It checks whether a String is a valid chapter, block, category or subcategory in ICD-10-CM.
     *
     * @param code is the String that must be checked
     * @return true if code is a valid ICD-10-CM code, otherwise false
     */
    public boolean isValidItem(String code){
        return codeToNode.containsKey(code) || code.length()>=4 && codeToNode.containsKey(code.substring(0,3)+"."+code.substring(3));
    }

    /**
     * It checks whether a String is a valid chapter in ICD-10-CM.
     *
     * @param code is the String that must be checked
     * @return true if code is a valid ICD-10-CM chapter, otherwise false
     */
    public boolean isChapter(String code){
        code = addDotToCode(code);
        if (codeToNode.containsKey(code)){
            return codeToNode.get(code).type.equals("chapter");
        }else{
            return false;
        }
    }

    /**
     * It checks whether a String is a valid block in ICD-10-CM.
     *
     * @param code is the String that must be checked
     * @return true if code is a valid ICD-10-CM block, otherwise false
     */
    public boolean isBlock(String code){
        code = addDotToCode(code);
        if (codeToNode.containsKey(code)){
            return codeToNode.get(code).type.equals("block") || (codeToNode.get(code).parent!=null && codeToNode.get(code).parent.name.equals(code));//second half of the or is for sections containing a single category
        }else{
            return false;
        }
    }

    /**
     * It checks whether a String is a valid category in ICD-10-CM.
     *
     * @param code is the String that must be checked
     * @return true if code is a valid ICD-10-CM category, otherwise false
     */
    public boolean isCategory(String code){
        code = addDotToCode(code);
        if (codeToNode.containsKey(code)){
            return codeToNode.get(code).type.equals("category");
        }else{
            return false;
        }
    }

    /**
     * It checks whether a String is a valid subcategory in ICD-10-CM.
     * By changing the value of includeExtendedSubcategories, it's possible to chose whether valid subcategories obtained by adding the 7th character to another code are included or not.
     *
     * @param code is the String that must be checked
     * @param includeExtendedSubcategories when it's true valid subcategories obtained by adding the 7th character to another code are considered as subcategories, otherwise they are not
     * @return true if code is a valid ICD-10-CM subcategory, otherwise false
     */
    public boolean isSubcategory(String code, boolean includeExtendedSubcategories){
        code = addDotToCode(code);
        if (codeToNode.containsKey(code)){
            return codeToNode.get(code).type.equals("subcategory") || (codeToNode.get(code).type.equals("extended subcategory") && includeExtendedSubcategories);
        }else{
            return false;
        }
    }

    /**
     * It checks whether a String is a valid subcategory in ICD-10-CM.
     * Valid subcategories obtained by adding the 7th character to another code are always included.
     *
     * @param code is the String that must be checked
     * @return true if code is a valid ICD-10-CM subcategory, otherwise false
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a>
     */
    public boolean isSubcategory(String code){
        return isSubcategory(code,true);
    }

    /**
     * It checks whether a String is a valid subcategory obtained by adding the 7th character to another code in ICD-10-CM.
     *
     * @param code is the String that must be checked
     * @return true if code is a valid ICD-10-CM subcategory, otherwise false
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> and <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#about-the-special-seventh-character">About the special seventh character</a>
     */
    public boolean isExtendedSubcategory(String code){
        code = addDotToCode(code);
        if (codeToNode.containsKey(code)){
            return codeToNode.get(code).type.equals("extended subcategory");
        }else{
            return false;
        }
    }

    /**
     * It checks whether a String is a valid chapter or block in ICD-10-CM.
     *
     * @param code is the String that must be checked
     * @return true if code is a valid ICD-10-CM chapter or block, otherwise false
     */
    public boolean isChapterOrBlock(String code){
        return isBlock(code)||isChapter(code);
    }

    /**
     * It checks whether a String is a valid category or subcategory in ICD-10-CM.
     *
     * @param code is the String that must be checked
     * @return true if code is a valid ICD-10-CM category or subcategory, otherwise false
     */
    public boolean isCategoryOrSubcategory(String code){
        return isCategory(code)||isSubcategory(code);
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns the description of said code.
     *
     * @param code is the ICD-10-CM code
     * @param prioritizeBlocks please see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#blocks-containing-only-one-category">Blocks containing only one category</a>
     * @return the description of code
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     */
    public String getDescription(String code, boolean prioritizeBlocks) throws IllegalArgumentException{
        if(!isValidItem(code)){
            throw new IllegalArgumentException("\""+code+"\" is not a valid ICD-10-CM code.");
        }
        ICDNode node = codeToNode.get(addDotToCode(code));
        if (prioritizeBlocks && node.parent!=null && node.parent.name.equals(node.name)){
            return node.parent.description;
        } else {
            return node.description;
        }
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns the description of said code.
     * Version of getDescription where the parameter prioritizeBlocks is implicitly false.
     * Please see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#blocks-containing-only-one-category">Blocks containing only one category</a> for the meaning of the missing parameter.
     *
     * @param code is the ICD-10-CM code
     * @return the description of code
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     */
    public String getDescription(String code) throws IllegalArgumentException{
        return getDescription(code,false);
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "excludes1" field of this code.
     * If this code does not have an "excludes1" field, it returns an empty ArrayList&lt;String&gt;.
     *
     * @param code is the ICD-10-CM code
     * @param prioritizeBlocks please see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#blocks-containing-only-one-category">Blocks containing only one category</a>
     * @return an ArrayList&lt;String&gt; containing the data of the "excludes1" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "excludes1" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> to learn more about the meaning of this field
     */
    public ArrayList<String> getExcludes1(String code, boolean prioritizeBlocks) throws IllegalArgumentException{
        if(!isValidItem(code)){
            throw new IllegalArgumentException("\""+code+"\" is not a valid ICD-10-CM code.");
        }
        ICDNode node = codeToNode.get(addDotToCode(code));
        if (prioritizeBlocks && node.parent!=null && node.parent.name.equals(node.name)){
            node = node.parent;
        }
        if(node.excludes1==null){
            return new ArrayList<String>();
        } else {
            return (ArrayList<String>) node.excludes1.clone();
        }
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "excludes1" field of this code.
     * If this code does not have an "excludes1" field, it returns an empty ArrayList&lt;String&gt;.
     * Version of getExcludes1 where the parameter prioritizeBlocks is implicitly false.
     * Please see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#blocks-containing-only-one-category">Blocks containing only one category</a> for the meaning of the missing parameter.
     *
     * @param code is the ICD-10-CM code
     * @return an ArrayList&lt;String&gt; containing the data of the "excludes1" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "excludes1" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> to learn more about the meaning of this field
     */
    public ArrayList<String> getExcludes1(String code) throws IllegalArgumentException{
        return getExcludes1(code,false);
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "excludes2" field of this code.
     * If this code does not have an "excludes2" field, it returns an empty ArrayList&lt;String&gt;.
     *
     * @param code is the ICD-10-CM code
     * @param prioritizeBlocks please see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#blocks-containing-only-one-category">Blocks containing only one category</a>
     * @return an ArrayList&lt;String&gt; containing the data of the "excludes2" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "excludes2" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> to learn more about the meaning of this field
     */
    public ArrayList<String> getExcludes2(String code, boolean prioritizeBlocks) throws IllegalArgumentException{
        if(!isValidItem(code)){
            throw new IllegalArgumentException("\""+code+"\" is not a valid ICD-10-CM code.");
        }
        ICDNode node = codeToNode.get(addDotToCode(code));
        if (prioritizeBlocks && node.parent!=null && node.parent.name.equals(node.name)){
            node = node.parent;
        }
        if(node.excludes2==null){
            return new ArrayList<String>();
        } else {
            return (ArrayList<String>) node.excludes2.clone();
        }
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "excludes2" field of this code.
     * If this code does not have an "excludes2" field, it returns an empty ArrayList&lt;String&gt;.
     * Version of getExcludes2 where the parameter prioritizeBlocks is implicitly false.
     * Please see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#blocks-containing-only-one-category">Blocks containing only one category</a> for the meaning of the missing parameter.
     *
     * @param code is the ICD-10-CM code
     * @return an ArrayList&lt;String&gt; containing the data of the "excludes2" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "excludes2" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> to learn more about the meaning of this field
     */
    public ArrayList<String> getExcludes2(String code) throws IllegalArgumentException{
        return getExcludes2(code,false);
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "includes" field of this code.
     * If this code does not have an "includes" field, it returns an empty ArrayList&lt;String&gt;.
     *
     * @param code is the ICD-10-CM code
     * @param prioritizeBlocks please see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#blocks-containing-only-one-category">Blocks containing only one category</a>
     * @return an ArrayList&lt;String&gt; containing the data of the "includes" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "includes" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> to learn more about the meaning of this field
     */
    public ArrayList<String> getIncludes(String code, boolean prioritizeBlocks) throws IllegalArgumentException{
        if(!isValidItem(code)){
            throw new IllegalArgumentException("\""+code+"\" is not a valid ICD-10-CM code.");
        }
        ICDNode node = codeToNode.get(addDotToCode(code));
        if (prioritizeBlocks && node.parent!=null && node.parent.name.equals(node.name)){
            node = node.parent;
        }
        if(node.includes==null){
            return new ArrayList<String>();
        } else {
            return (ArrayList<String>) node.includes.clone();
        }
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "includes" field of this code.
     * If this code does not have an "includes" field, it returns an empty ArrayList&lt;String&gt;.
     * Version of getIncludes where the parameter prioritizeBlocks is implicitly false.
     * Please see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#blocks-containing-only-one-category">Blocks containing only one category</a> for the meaning of the missing parameter.
     *
     * @param code is the ICD-10-CM code
     * @return an ArrayList&lt;String&gt; containing the data of the "includes" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "includes" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> to learn more about the meaning of this field
     */
    public ArrayList<String> getIncludes(String code) throws IllegalArgumentException{
        return getIncludes(code,false);
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "inclusionTerm" field of this code.
     * If this code does not have an "inclusionTerm" field, it returns an empty ArrayList&lt;String&gt;.
     *
     * @param code is the ICD-10-CM code
     * @param prioritizeBlocks please see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#blocks-containing-only-one-category">Blocks containing only one category</a>
     * @return an ArrayList&lt;String&gt; containing the data of the "inclusionTerm" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "inclusionTerm" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     */
    public ArrayList<String> getInclusionTerm(String code, boolean prioritizeBlocks) throws IllegalArgumentException{
        if(!isValidItem(code)){
            throw new IllegalArgumentException("\""+code+"\" is not a valid ICD-10-CM code.");
        }
        ICDNode node = codeToNode.get(addDotToCode(code));
        if (prioritizeBlocks && node.parent!=null && node.parent.name.equals(node.name)){
            node = node.parent;
        }
        if(node.inclusionTerm==null){
            return new ArrayList<String>();
        } else {
            return (ArrayList<String>) node.inclusionTerm.clone();
        }
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "inclusionTerm" field of this code.
     * If this code does not have an "inclusionTerm" field, it returns an empty ArrayList&lt;String&gt;.
     * Version of getInclusionTerm where the parameter prioritizeBlocks is implicitly false.
     * Please see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#blocks-containing-only-one-category">Blocks containing only one category</a> for the meaning of the missing parameter.
     *
     * @param code is the ICD-10-CM code
     * @return an ArrayList&lt;String&gt; containing the data of the "inclusionTerm" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "inclusionTerm" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     */
    public ArrayList<String> getInclusionTerm(String code) throws IllegalArgumentException{
        return getInclusionTerm(code,false);
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns a String containing the data of the "sevenChrNote" field of this code.
     * If this code does not have an "sevenChrNote" field, it returns an empty String.
     *
     * @param code is the ICD-10-CM code
     * @param searchInAncestors if it's set to true, if the given code doesn't have a "sevenChrNote" field but one of its ancestor does, the "sevenChrNote" data of the closer ancestor that contains such a field is returned
     * @param prioritizeBlocks please see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#blocks-containing-only-one-category">Blocks containing only one category</a>
     * @return a String containing the data of the "sevenChrNote" field of this code, an empty String if this code does not have an "sevenChrNote" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> and <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#about-the-special-seventh-character">About the special seventh character</a> to learn more about the meaning of this field
     */
    public String getSevenChrNote(String code,boolean searchInAncestors, boolean prioritizeBlocks) throws IllegalArgumentException{
        if(!isValidItem(code)){
            throw new IllegalArgumentException("\""+code+"\" is not a valid ICD-10-CM code.");
        }
        ICDNode node = codeToNode.get(addDotToCode(code));
        if (prioritizeBlocks && node.parent!=null && node.parent.name.equals(node.name)){
            node = node.parent;
        }
        if(searchInAncestors && node.sevenChrNote.equals("") && node.sevenChrNoteAncestor!=null){
            return node.sevenChrNoteAncestor.sevenChrNote;
        } else {
            return node.sevenChrNote;
        }
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns a String containing the data of the "sevenChrNote" field of this code.
     * If this code does not have an "sevenChrNote" field, it returns an empty String.
     * Version of getSevenChrNote where the parameters searchInAncestors and prioritizeBlocks are implicitly false.
     * Please see {@link #getSevenChrNote(String, boolean, boolean)} for the meaning of the missing parameters.
     *
     * @param code is the ICD-10-CM code
     * @return a String containing the data of the "sevenChrNote" field of this code, an empty String if this code does not have an "sevenChrNote" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> and <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#about-the-special-seventh-character">About the special seventh character</a> to learn more about the meaning of this field
     */
    public String getSevenChrNote(String code) throws IllegalArgumentException{
        return getSevenChrNote(code,false,false);
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns a HashMap&lt;String, String&gt; containing the data of the "sevenChrDef" field of this code.
     * If this code does not have an "sevenChrDef" field, it returns an empty HashMap&lt;String, String&gt;.
     *
     * @param code is the ICD-10-CM code
     * @param searchInAncestors if it's set to true, if the given code doesn't have a "sevenChrDef" field but one of its ancestor does, the "sevenChrDef" data of the closer ancestor that contains such a field is returned
     * @param prioritizeBlocks please see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#blocks-containing-only-one-category">Blocks containing only one category</a>
     * @return a HashMap&lt;String, String&gt; containing the data of the "sevenChrDef" field of this code, an empty HashMap&lt;String, String&gt; if this code does not have an "sevenChrDef" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> and <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#about-the-special-seventh-character">About the special seventh character</a> to learn more about the meaning of this field
     */
    public HashMap<String, String> getSevenChrDef(String code,boolean searchInAncestors, boolean prioritizeBlocks) throws IllegalArgumentException{
        if(!isValidItem(code)){
            throw new IllegalArgumentException("\""+code+"\" is not a valid ICD-10-CM code.");
        }
        ICDNode node = codeToNode.get(addDotToCode(code));
        if (prioritizeBlocks && node.parent!=null && node.parent.name.equals(node.name)){
            node = node.parent;
        }
        HashMap<String, String> result;
        if(searchInAncestors && node.sevenChrDef==null && node.sevenChrDefAncestor!=null){
            result = node.sevenChrDefAncestor.sevenChrDef;
        } else {
            result = node.sevenChrDef;
        }
        if(result==null){
            return new HashMap<>();
        } else {
            return (HashMap<String, String>) result.clone();
        }
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns a HashMap&lt;String, String&gt; containing the data of the "sevenChrDef" field of this code.
     * If this code does not have an "sevenChrDef" field, it returns an empty HashMap&lt;String, String&gt;.
     * Version of getSevenChrDef where the parameters searchInAncestors and prioritizeBlocks are implicitly false.
     * Please see {@link #getSevenChrDef(String, boolean, boolean)} for the meaning of the missing parameters.
     *
     * @param code is the ICD-10-CM code
     * @return a HashMap&lt;String, String&gt; containing the data of the "sevenChrDef" field of this code, an empty HashMap&lt;String, String&gt; if this code does not have an "sevenChrDef" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> and <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#about-the-special-seventh-character">About the special seventh character</a> to learn more about the meaning of this field
     */
    public HashMap<String, String> getSevenChrDef(String code) throws IllegalArgumentException{
        return getSevenChrDef(code,false,false);
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns a String containing the data of the "useAdditionalCode" field of this code.
     * If this code does not have an "useAdditionalCode" field, it returns an empty String.
     *
     * @param code is the ICD-10-CM code
     * @param searchInAncestors if it's set to true, if the given code doesn't have a "useAdditionalCode" field but one of its ancestor does, the "useAdditionalCode" data of the closer ancestor that contains such a field is returned
     * @param prioritizeBlocks please see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#blocks-containing-only-one-category">Blocks containing only one category</a>
     * @return a String containing the data of the "useAdditionalCode" field of this code, an empty String if this code does not have an "useAdditionalCode" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> to learn more about the meaning of this field
     */
    public String getUseAdditionalCode(String code,boolean searchInAncestors, boolean prioritizeBlocks) throws IllegalArgumentException{
        if(!isValidItem(code)){
            throw new IllegalArgumentException("\""+code+"\" is not a valid ICD-10-CM code.");
        }
        ICDNode node = codeToNode.get(addDotToCode(code));
        if (prioritizeBlocks && node.parent!=null && node.parent.name.equals(node.name)){
            node = node.parent;
        }
        if(searchInAncestors && node.useAdditionalCode.equals("") && node.useAdditionalCodeAncestor!=null){
            return node.useAdditionalCodeAncestor.useAdditionalCode;
        } else {
            return node.useAdditionalCode;
        }
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns a String containing the data of the "useAdditionalCode" field of this code.
     * If this code does not have an "useAdditionalCode" field, it returns an empty String.
     * Version of getUseAdditionalCode where the parameters searchInAncestors and prioritizeBlocks are implicitly false.
     * Please see {@link #getUseAdditionalCode(String, boolean, boolean)} for the meaning of the missing parameters.
     *
     * @param code is the ICD-10-CM code
     * @return a String containing the data of the "useAdditionalCode" field of this code, an empty String if this code does not have an "useAdditionalCode" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> to learn more about the meaning of this field
     */
    public String getUseAdditionalCode(String code) throws IllegalArgumentException{
        return getUseAdditionalCode(code,false,false);
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns a String containing the data of the "codeFirst" field of this code.
     * If this code does not have an "codeFirst" field, it returns an empty String.
     *
     * @param code is the ICD-10-CM code
     * @param searchInAncestors if it's set to true, if the given code doesn't have a "codeFirst" field but one of its ancestor does, the "codeFirst" data of the closer ancestor that contains such a field is returned
     * @param prioritizeBlocks please see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#blocks-containing-only-one-category">Blocks containing only one category</a>
     * @return a String containing the data of the "codeFirst" field of this code, an empty String if this code does not have an "codeFirst" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> to learn more about the meaning of this field
     */
    public String getCodeFirst(String code,boolean searchInAncestors, boolean prioritizeBlocks) throws IllegalArgumentException{
        if(!isValidItem(code)){
            throw new IllegalArgumentException("\""+code+"\" is not a valid ICD-10-CM code.");
        }
        ICDNode node = codeToNode.get(addDotToCode(code));
        if (prioritizeBlocks && node.parent!=null && node.parent.name.equals(node.name)){
            node = node.parent;
        }
        if(searchInAncestors && node.codeFirst.equals("") && node.codeFirstAncestor!=null){
            return node.codeFirstAncestor.codeFirst;
        } else {
            return node.codeFirst;
        }
    }

    /**
     * Given a String that contains an ICD-10-CM code, it returns a String containing the data of the "codeFirst" field of this code.
     * If this code does not have an "codeFirst" field, it returns an empty String.
     * Version of getCodeFirst where the parameters searchInAncestors and prioritizeBlocks are implicitly false.
     * Please see {@link #getCodeFirst(String, boolean, boolean)} for the meaning of the missing parameters.
     *
     * @param code is the ICD-10-CM code
     * @return a String containing the data of the "codeFirst" field of this code, an empty String if this code does not have an "codeFirst" field
     * @throws IllegalArgumentException if code is not a valid ICD-10-CM code
     * @see <a href="https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md">Instructional Notations</a> to learn more about the meaning of this field
     */
    public String getCodeFirst(String code) throws IllegalArgumentException{
        return getCodeFirst(code,false,false);
    }
}
