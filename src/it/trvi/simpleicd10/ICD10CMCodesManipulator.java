package it.trvi.simpleicd10;
import it.trvi.easyxml.*;

public class ICD10CMCodesManipulator {
    public static void main(String[] args) {
        ICD10CMCodesManipulator c = new ICD10CMCodesManipulator();
    }


    /**
     * The constructor that reads the data from the xml file to load all the data relative to the ICD-10-CM classification.
     */
    public ICD10CMCodesManipulator(){
        XMLElement xmlData;
        try {
            xmlData = XMLTreeBuilder.buildFromStream(getClass().getResourceAsStream("icd10cm_tabular_2021.xml"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
