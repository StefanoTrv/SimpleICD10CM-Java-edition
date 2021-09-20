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


}
