# SimpleICD10CM-Java-edition
A simple Java library for ICD-10-CM codes

## Index
* [Release notes](#release-notes)
* [Introduction](#introduction)
* [The format of the codes](#the-format-of-the-codes)
* [About the file "Instructional Notations.md"](#about-the-file-instructional-notationsmd)
* [Blocks containing only one category](#blocks-containing-only-one-category)
* [About the special seventh character](#about-the-special-seventh-character)
* [Documentation](#documentation)
  <details>
  <summary>Click to expand</summary>
  
  * [ICD10CMCodesManipulator()](#icd10cmcodesmanipulator)
  * [boolean isValidItem(String code)](#boolean-isvaliditemstring-code)
  * [boolean isChapter(String code)](#boolean-ischapterstring-code)
  * [boolean isBlock(String code)](#boolean-isblockstring-code)
  * [boolean isCategory(String code)](#boolean-iscategorystring-code)
  * [boolean isSubcategory(String code, boolean includeExtendedSubcategories)](#boolean-issubcategorystring-code-boolean-includeextendedsubcategories)
  * [boolean isSubcategory(String code)](#boolean-issubcategorystring-code)
  * [boolean isExtendedSubcategory(String code)](#boolean-isextendedsubcategorystring-code)
  * [boolean isChapterOrBlock(String code)](#boolean-ischapterorblockstring-code)
  * [boolean isCategoryOrSubcategory(String code)](#boolean-iscategoryorsubcategorystring-code)
  * [String getDescription(String code, boolean prioritizeBlocks)](#string-getdescriptionstring-code-boolean-prioritizeblocks)
  * [String getDescription(String code)](#string-getdescriptionstring-code)
  * [ArrayList&lt;String&gt; getExcludes1(String code, boolean prioritizeBlocks)](#arrayliststring-getexcludes1string-code-boolean-prioritizeblocks)
  * [ArrayList&lt;String&gt; getExcludes1(String code)](#arrayliststring-getexcludes1string-code)
  * [ArrayList&lt;String&gt; getExcludes2(String code, boolean prioritizeBlocks)](#arrayliststring-getexcludes2string-code-boolean-prioritizeblocks)
  * [ArrayList&lt;String&gt; getExcludes2(String code)](#arrayliststring-getexcludes2string-code)
  * [ArrayList&lt;String&gt; getIncludes(String code, boolean prioritizeBlocks)](#arrayliststring-getincludesstring-code-boolean-prioritizeblocks)
  * [ArrayList&lt;String&gt; getIncludes(String code)](#arrayliststring-getincludesstring-code)
  * [ArrayList&lt;String&gt; getInclusionTerm(String code, boolean prioritizeBlocks)](#arrayliststring-getinclusiontermstring-code-boolean-prioritizeblocks)
  * [ArrayList&lt;String&gt; getInclusionTerm(String code)](#arrayliststring-getinclusiontermstring-code)
  * [String getSevenChrNote(String code,boolean searchInAncestors, boolean prioritizeBlocks)](#string-getsevenchrnotestring-codeboolean-searchinancestors-boolean-prioritizeblocks)
  * [String getSevenChrNote(String code)](#string-getsevenchrnotestring-code)
  * [HashMap&lt;String, String&gt; getSevenChrDef(String code,boolean searchInAncestors, boolean prioritizeBlocks)](#hashmapstring-string-getsevenchrdefstring-codeboolean-searchinancestors-boolean-prioritizeblocks)
  * [HashMap&lt;String, String&gt; getSevenChrDef(String code)](#hashmapstring-string-getsevenchrdefstring-code)
  * [String getUseAdditionalCode(String code,boolean searchInAncestors, boolean prioritizeBlocks)](#string-getuseadditionalcodestring-codeboolean-searchinancestors-boolean-prioritizeblocks)
  * [String getUseAdditionalCode(String code)](#string-getuseadditionalcodestring-code)
  * [String getCodeFirst(String code,boolean searchInAncestors, boolean prioritizeBlocks)](#string-getcodefirststring-codeboolean-searchinancestors-boolean-prioritizeblocks)
  * [String getCodeFirst(String code)](#string-getcodefirststring-code)
  * [String getFullData(String code,boolean searchInAncestors, boolean prioritizeBlocks)](#string-getfulldatastring-codeboolean-searchinancestors-boolean-prioritizeblocks)
  * [String getFullData(String code)](#string-getfulldatastring-code)
  * [String getParent(String code, boolean prioritizeBlocks)](#string-getparentstring-code-boolean-prioritizeblocks)
  * [String getParent(String code)](#string-getparentstring-code)
  * [ArrayList&lt;String&gt; getChildren(String code, boolean prioritizeBlocks)](#arrayliststring-getchildrenstring-code-boolean-prioritizeblocks)
  * [ArrayList&lt;String&gt; getChildren(String code)](#arrayliststring-getchildrenstring-code)
  * [ArrayList&lt;String&gt; getAncestors(String code, boolean prioritizeBlocks)](#arrayliststring-getancestorsstring-code-boolean-prioritizeblocks)
  * [ArrayList&lt;String&gt; getAncestors(String code)](#arrayliststring-getancestorsstring-code)
  * [ArrayList&lt;String&gt; getDescendants(String code, boolean prioritizeBlocks)](#arrayliststring-getdescendantsstring-code-boolean-prioritizeblocks)
  * [ArrayList&lt;String&gt; getDescendants(String code)](#arrayliststring-getdescendantsstring-code)
  * [boolean isAncestor(String a, String b, boolean prioritizeBlocksA, boolean prioritizeBlocksB)](#boolean-isancestorstring-a-string-b-boolean-prioritizeblocksa-boolean-prioritizeblocksb)
  * [boolean isAncestor(String a, String b)](#boolean-isancestorstring-a-string-b)
  * [boolean isDescendant(String a, String b, boolean prioritizeBlocksA, boolean prioritizeBlocksB)](#boolean-isdescendantstring-a-string-b-boolean-prioritizeblocksa-boolean-prioritizeblocksb)
  * [boolean isDescendant(String a, String b)](#boolean-isdescendantstring-a-string-b)
  * [String getNearestCommonAncestor(String a, String b, boolean prioritizeBlocksA, boolean prioritizeBlocksB)](#string-getnearestcommonancestorstring-a-string-b-boolean-prioritizeblocksa-boolean-prioritizeblocksb)
  * [String getNearestCommonAncestor(String a, String b)](#string-getnearestcommonancestorstring-a-string-b)
  * [boolean isLeaf(String code, boolean prioritizeBlocks)](#boolean-isleafstring-code-boolean-prioritizeblocks)
  * [boolean isLeaf(String code)](#boolean-isleafstring-code)
  * [ArrayList&lt;String&gt; getAllCodes(boolean withDots)](#arrayliststring-getallcodesboolean-withdots)
  * [ArrayList&lt;String&gt; getAllCodes()](#arrayliststring-getallcodes)
  * [int getIndex(String code)](#int-getindexstring-code)
  * [String removeDot(String code)](#string-removedotstring-code)
  * [String addDot(String code)](#string-adddotstring-code)
</details>

* [Conclusion](#conclusion)

## Release notes
`none yet`

## Introduction
The objective of this library is to provide a simple instrument for dealing with **ICD-10-CM** codes in Java. It provides ways to check whether a code exists, find its ancestors and descendants, see the data associated with it, including its description, and much more.  
If you are looking for a library that deals with ICD-10 codes instead of ICD-10-CM codes, you can check the [SimpleICD10-Java-edition](https://github.com/StefanoTrv/SimpleICD10-Java-edition), which is based on the 2019 version of ICD-10.  
If you are looking for a Python version of this project, you can check the [simple_icd_10_cm](https://github.com/StefanoTrv/simple_icd_10_CM) library.

The data used in this library was taken from the websites of the CDC and of the CMS. This library currently uses the **January 2021 release of ICD-10-CM**.

All the classes in this library are contained in the package "`it.trvi.simpleicd10`".

## The format of the codes
The codes of subcategories can be written in two different ways: with a dot (for example "I13.1") and with no dot (for example "I131"). The methods in this library can receive as input codes in both these formats. The codes returned by the functions will always be in the format with the dot.  
You can easily change the format of a code by using the [`removeDot`](#removedotstring-code) and [`addDot`](#adddotstring-code) functions.

## About the file "Instructional Notations.md"
The file [Instructional Notations.md](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) contains the introduction present in the file `icd10cm_tabular_2021.xml` (the file that contains the whole ICD-10-CM classification), copied there in a more accessible and readable format. There you can find an explanation about the meaning of most of the additional fields that can accompany a code.

## Blocks containing only one category
Unlike ICD-10, ICD-10-CM includes blocks of categories that contain only one category (and its subcategories). These blocks are named after the category that they contain, which means that ICD-10-CM contains blocks and categories that have the same exact code. This is a problem: because of this questionable decision, we can't know for sure if the code "B99", for example, refers to the category "B99" or to the block with the same name. This can be seen in the following example, where "B99" is recognized as both a block and a category:
```Java
cm.isBlock("B99")
//true
cm.isCategory("B99")
//true
```
To solve this ambiguity, I've introduced in most methods the parameter `prioritizeBlocks`. This parameter has an effect only when the string passed as input could be the name of a category or of its parent block: when this ambiguity is not present, the value of this parameter won't have any impact on the computation. When `prioritizeBlocks` is false, which is the default value when its omitted by using overloading, the ambiguous code will be interpreted as the category, when it's set to true the same code will be interpreted as being the block. The following code shows an example of this in action:
```Java
cm.getChildren("B99")
//[B99.8, B99.9]
cm.getChildren("B99",true)
//[B99]
```
If you want to know if a specific code is ambiguous, it's pretty simple: you just have to check if it's at the same time a block and a category, as in the following examples:
```Java
cm.isBlock("B10") && cm.isCategory("B10")
//true
cm.isBlock("I12") && cm.isCategory("I12")
//false
```

## About the special seventh character
The file `icd10cm_tabular_2021.xml`, which is the XML file that contains the whole ICD-10-CM classification, doesn't have an entry for all the codes generated by adding the "special" seventh character, but it often contains instead rules that explain how to generate these codes in the "sevenChrDef" field (and sometimes in the "sevenChrNote" field too, just to complicate things a little bit...). You can find more about the structure of these particular codes in [Instructional Notations.md](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md).  
Due to the lack of a complete entry for these codes, I had to decide how they would be handled in this library. So I decided that their only field would be the description, composed of the description of their parent followed by the description of the meaning of the additional character, with a comma between the two: this description appears in official documents about ICD-10-CM (for example in the file `icd10cm-order-Jan-2021.txt`), so it's not my invention but the actual official format. All the other fields are empty, but the optional argument `searchInAncestors` of certain functions can be used to automatically retrieve the content of certain fields from the ancestors of the code (see the description of the specific functions in the [Documentation](#documentation) for more details).  
If you need to know whether a code has been automatically generated using a rule described in a "sevenChrDef" field, you can use the [`isExtendedSubcategory`](#isextendedsubcategorystring-code) method.

## Documentation
This library is comprised of a single class, ICD10CMCodesManipulator. When an object of this class is instantiated, it loads all the relevant data and creates the appropriate data structures to work effectively with it. It's through these objects that the functionalities of this library can be accessed.  
This class is contained in the package "`it.trvi.simpleicd10`".
```Java
ICD10CMCodesManipulator cm = new ICD10CMCodesManipulator();
```

### ICD10CMCodesManipulator()
The constructor that reads the data from the XML file to load all the data relative to the ICD-10-CM classification.

### boolean isValidItem(String code)
It checks whether a String is a valid chapter, block, category or subcategory in ICD-10-CM.

Parameters:  
&ensp;&ensp;`code` is the String that must be checked  
Returns:  
&ensp;&ensp;true if code is a valid ICD-10-CM code, otherwise false

### boolean isChapter(String code)
It checks whether a String is a valid chapter in ICD-10-CM.

Parameters:  
&ensp;&ensp;`code` is the String that must be checked  
Returns:  
&ensp;&ensp;true if code is a valid ICD-10-CM chapter, otherwise false

### boolean isBlock(String code)
It checks whether a String is a valid block in ICD-10-CM.

Parameters:  
&ensp;&ensp;`code` is the String that must be checked  
Returns:  
&ensp;&ensp;true if code is a valid ICD-10-CM block, otherwise false

### boolean isCategory(String code)
It checks whether a String is a valid category in ICD-10-CM.

Parameters:  
&ensp;&ensp;`code` is the String that must be checked  
Returns:  
&ensp;&ensp;true if code is a valid ICD-10-CM category, otherwise false

### boolean isSubcategory(String code, boolean includeExtendedSubcategories)
It checks whether a String is a valid subcategory in ICD-10-CM.
By changing the value of `includeExtendedSubcategories`, it's possible to chose whether valid subcategories obtained by adding the 7th character to another code are included or not (see [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) for more information).

Parameters:  
&ensp;&ensp;`code` is the String that must be checked  
&ensp;&ensp;`includeExtendedSubcategories` when it's true valid subcategories obtained by adding the 7th character to another code are considered as subcategories, otherwise they are not  
Returns:  
&ensp;&ensp;true if code is a valid ICD-10-CM subcategory, otherwise false

### boolean isSubcategory(String code)
It checks whether a String is a valid subcategory in ICD-10-CM.
Valid subcategories obtained by adding the 7th character to another code are always included (see [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) for more information).

Parameters:  
&ensp;&ensp;`code` is the String that must be checked  
Returns:  
&ensp;&ensp;true if code is a valid ICD-10-CM subcategory, otherwise false

### boolean isExtendedSubcategory(String code)
It checks whether a String is a valid subcategory obtained by adding the 7th character to another code in ICD-10-CM (see [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) and [About the special seventh character](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#about-the-special-seventh-character) for more information).

Parameters:  
&ensp;&ensp;`code` is the String that must be checked  
Returns:  
&ensp;&ensp;true if code is a valid ICD-10-CM subcategory, otherwise false

### boolean isChapterOrBlock(String code)
It checks whether a String is a valid chapter or block in ICD-10-CM.

Parameters:  
&ensp;&ensp;`code` is the String that must be checked  
Returns:  
&ensp;&ensp;true if code is a valid ICD-10-CM chapter or block, otherwise false

### boolean isCategoryOrSubcategory(String code)
It checks whether a String is a valid category or subcategory in ICD-10-CM.

Parameters:  
&ensp;&ensp;`code` is the String that must be checked  
Returns:  
&ensp;&ensp;true if code is a valid ICD-10-CM category or subcategory, otherwise false

### String getDescription(String code, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it returns the description of said code.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;the description of code  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### String getDescription(String code)
Given a String that contains an ICD-10-CM code, it returns the description of said code.
Version of `getDescription` where the parameter `prioritizeBlocks` is implicitly false.
Please see [Blocks containing only one category](#blocks-containing-only-one-category) for the meaning of the missing parameter.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;the description of code  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getExcludes1(String code, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "excludes1" field of this code.
If this code does not have an "excludes1" field, it returns an empty ArrayList&lt;String&gt;.
Please see [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) if you have doubts about the meaning of this field.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;an ArrayList&lt;String&gt; containing the data of the "excludes1" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "excludes1" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getExcludes1(String code)
Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "excludes1" field of this code.
If this code does not have an "excludes1" field, it returns an empty ArrayList&lt;String&gt;.
Version of `getExcludes1` where the parameter `prioritizeBlocks` is implicitly false.
Please see [Blocks containing only one category](#blocks-containing-only-one-category) for the meaning of the missing parameter and [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) if you have doubts about the meaning of this field.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;an ArrayList&lt;String&gt; containing the data of the "excludes1" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "excludes1" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getExcludes2(String code, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "excludes2" field of this code.
If this code does not have an "excludes2" field, it returns an empty ArrayList&lt;String&gt;.
Please see [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) if you have doubts about the meaning of this field.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;an ArrayList&lt;String&gt; containing the data of the "excludes2" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "excludes2" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getExcludes2(String code)
Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "excludes2" field of this code.
If this code does not have an "excludes2" field, it returns an empty ArrayList&lt;String&gt;.
Version of `getExcludes2` where the parameter `prioritizeBlocks` is implicitly false.
Please see [Blocks containing only one category](#blocks-containing-only-one-category) for the meaning of the missing parameter and [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) if you have doubts about the meaning of this field.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;an ArrayList&lt;String&gt; containing the data of the "excludes2" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "excludes2" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getIncludes(String code, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "includes" field of this code.
If this code does not have an "includes" field, it returns an empty ArrayList&lt;String&gt;.
Please see [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) if you have doubts about the meaning of this field.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;an ArrayList&lt;String&gt; containing the data of the "includes" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "includes" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getIncludes(String code)
Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "includes" field of this code.
If this code does not have an "includes" field, it returns an empty ArrayList&lt;String&gt;.
Version of `getIncludes` where the parameter `prioritizeBlocks` is implicitly false.
Please see [Blocks containing only one category](#blocks-containing-only-one-category) for the meaning of the missing parameter and [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) if you have doubts about the meaning of this field.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;an ArrayList&lt;String&gt; containing the data of the "includes" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "includes" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getInclusionTerm(String code, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "inclusionTerm" field of this code.
If this code does not have an "inclusionTerm" field, it returns an empty ArrayList&lt;String&gt;.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;an ArrayList&lt;String&gt; containing the data of the "inclusionTerm" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "inclusionTerm" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getInclusionTerm(String code)
Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing the data of the "inclusionTerm" field of this code.
If this code does not have an "inclusionTerm" field, it returns an empty ArrayList&lt;String&gt;.
Version of `getInclusionTerm` where the parameter `prioritizeBlocks` is implicitly false.
Please see [Blocks containing only one category](#blocks-containing-only-one-category) for the meaning of the missing parameter.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;an ArrayList&lt;String&gt; containing the data of the "inclusionTerm" field of this code, an empty ArrayList&lt;String&gt; if this code does not have an "inclusionTerm" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### String getSevenChrNote(String code,boolean searchInAncestors, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it returns a String containing the data of the "sevenChrNote" field of this code.
If this code does not have an "sevenChrNote" field, it returns an empty String.
Please see [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) and [About the special seventh character](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#about-the-special-seventh-character) if you have doubts about the meaning of this field.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`searchInAncestors` if it's set to true, if the given code doesn't have a "sevenChrNote" field but one of its ancestor does, the "sevenChrNote" data of the closer ancestor that contains such a field is returned  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;a String containing the data of the "sevenChrNote" field of this code, an empty String if this code does not have an "sevenChrNote" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### String getSevenChrNote(String code)
Given a String that contains an ICD-10-CM code, it returns a String containing the data of the "sevenChrNote" field of this code.
If this code does not have an "sevenChrNote" field, it returns an empty String.
Version of `getSevenChrNote` where the parameters `searchInAncestors` and `prioritizeBlocks` are implicitly false.
Please see [getSevenChrNote(String, boolean, boolean)](getsevenchrnotestring-boolean-boolean) for the meaning of the missing parameters and [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) and [About the special seventh character](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#about-the-special-seventh-character) if you have doubts about the meaning of this field.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;a String containing the data of the "sevenChrNote" field of this code, an empty String if this code does not have an "sevenChrNote" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### HashMap&lt;String, String&gt; getSevenChrDef(String code,boolean searchInAncestors, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it returns a HashMap&lt;String, String&gt; containing the data of the "sevenChrDef" field of this code.
If this code does not have an "sevenChrDef" field, it returns an empty HashMap&lt;String, String&gt;.
Please see [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) and [About the special seventh character](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#about-the-special-seventh-character) if you have doubts about the meaning of this field.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`searchInAncestors` if it's set to true, if the given code doesn't have a "sevenChrDef" field but one of its ancestor does, the "sevenChrDef" data of the closer ancestor that contains such a field is returned  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;a HashMap&lt;String, String&gt; containing the data of the "sevenChrDef" field of this code, an empty HashMap&lt;String, String&gt; if this code does not have an "sevenChrDef" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### HashMap&lt;String, String&gt; getSevenChrDef(String code)
Given a String that contains an ICD-10-CM code, it returns a HashMap&lt;String, String&gt; containing the data of the "sevenChrDef" field of this code.
If this code does not have an "sevenChrDef" field, it returns an empty HashMap&lt;String, String&gt;.
Version of `getSevenChrDef` where the parameters `searchInAncestors` and `prioritizeBlocks` are implicitly false.
Please see [getSevenChrDef(String, boolean, boolean)](getsevenchrdefstring-boolean-boolean) for the meaning of the missing parameters and [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) and [About the special seventh character](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition#about-the-special-seventh-character) if you have doubts about the meaning of this field.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;a HashMap&lt;String, String&gt; containing the data of the "sevenChrDef" field of this code, an empty HashMap&lt;String, String&gt; if this code does not have an "sevenChrDef" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### String getUseAdditionalCode(String code,boolean searchInAncestors, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it returns a String containing the data of the "useAdditionalCode" field of this code.
If this code does not have an "useAdditionalCode" field, it returns an empty String.
Please see [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) if you have doubts about the meaning of this field.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`searchInAncestors` if it's set to true, if the given code doesn't have a "useAdditionalCode" field but one of its ancestor does, the "useAdditionalCode" data of the closer ancestor that contains such a field is returned  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;a String containing the data of the "useAdditionalCode" field of this code, an empty String if this code does not have an "useAdditionalCode" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### String getUseAdditionalCode(String code)
Given a String that contains an ICD-10-CM code, it returns a String containing the data of the "useAdditionalCode" field of this code.
If this code does not have an "useAdditionalCode" field, it returns an empty String.
Version of `getUseAdditionalCode` where the parameters `searchInAncestors` and `prioritizeBlocks` are implicitly false.
Please see [getUseAdditionalCode(String, boolean, boolean)](getuseadditionalcodestring-boolean-boolean) for the meaning of the missing parameters and [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) if you have doubts about the meaning of this field.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;a String containing the data of the "useAdditionalCode" field of this code, an empty String if this code does not have an "useAdditionalCode" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### String getCodeFirst(String code,boolean searchInAncestors, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it returns a String containing the data of the "codeFirst" field of this code.
If this code does not have an "codeFirst" field, it returns an empty String.
Please see [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) if you have doubts about the meaning of this field.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`searchInAncestors` if it's set to true, if the given code doesn't have a "codeFirst" field but one of its ancestor does, the "codeFirst" data of the closer ancestor that contains such a field is returned  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;a String containing the data of the "codeFirst" field of this code, an empty String if this code does not have an "codeFirst" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### String getCodeFirst(String code)
Given a String that contains an ICD-10-CM code, it returns a String containing the data of the "codeFirst" field of this code.
If this code does not have an "codeFirst" field, it returns an empty String.
Version of `getCodeFirst` where the parameters `searchInAncestors` and `prioritizeBlocks` are implicitly false.
Please see [getCodeFirst(String, boolean, boolean)](getcodefirststring-boolean-boolean) for the meaning of the missing parameters and [Instructional Notations](https://github.com/StefanoTrv/SimpleICD10CM-Java-edition/blob/master/Instructional%20Notations.md) if you have doubts about the meaning of this field.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;a String containing the data of the "codeFirst" field of this code, an empty String if this code does not have an "codeFirst" field  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### String getFullData(String code,boolean searchInAncestors, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it returns a String containing all the available data of the code.
The empty fields are omitted from the String, except for the list of children.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`searchInAncestors` if it's set to true, if the given code doesn't have a certain field but one of its ancestor does, the data of the closer ancestor that contains such a field is returned  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp; a String containing all the available data of the code  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### String getFullData(String code)
Given a String that contains an ICD-10-CM code, it returns a String containing all the available data of the code.
The empty fields are omitted from the String, except for the list of children.
Version of `getFullData` where the parameters `searchInAncestors` and `prioritizeBlocks` are implicitly false.
Please see [getFullData(String, boolean, boolean)](getfulldatastring-boolean-boolean) for the meaning of the missing parameters.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;a String containing all the available data of the code  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### String getParent(String code, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it returns a String containing its parent in the ICD-10-CM classification.
If the code doesn't have a parent (that is, if it's a chapter), it returns an empty String.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;a String containing the parent of the code, or an empty String if it does not have a parent  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### String getParent(String code)
Given a String that contains an ICD-10-CM code, it returns a String containing its parent in the ICD-10-CM classification.
If the code doesn't have a parent (that is, if it's a chapter), it returns an empty String.
Version of `getParent` where the parameter `prioritizeBlocks` is implicitly false.
Please see [Blocks containing only one category](#blocks-containing-only-one-category) for the meaning of the missing parameter.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;a String containing the parent of the code, or an empty String if it does not have a parent  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getChildren(String code, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing its children in the ICD-10-CM classification.
If the code doesn't have any children, it returns an empty ArrayList&lt;String&gt;.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;an ArrayList&lt;String&gt; of strings containing its children, or an empty ArrayList&lt;String&gt; if it does not have any children  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getChildren(String code)
Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing its children in the ICD-10-CM classification.
If the code doesn't have any children, it returns an empty ArrayList&lt;String&gt;.
Version of `getParent` where the parameter `prioritizeBlocks` is implicitly false.
Please see [Blocks containing only one category](#blocks-containing-only-one-category) for the meaning of the missing parameter.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;an ArrayList&lt;String&gt; of strings containing its children, or an empty ArrayList&lt;String&gt; if it does not have any children  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getAncestors(String code, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing all its ancestors in the ICD-10-CM classification.
The results are ordered from its parent to its most distant ancestor.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;an ArrayList&lt;String&gt; containing the ancestors of code  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getAncestors(String code)
Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing all its ancestors in the ICD-10-CM classification.
The results are ordered from its parent to its most distant ancestor.
Version of `getAncestors` where the parameter `prioritizeBlocks` is implicitly false.
Please see [Blocks containing only one category](#blocks-containing-only-one-category) for the meaning of the missing parameter.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;an ArrayList&lt;String&gt; containing the ancestors of code  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getDescendants(String code, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing all its descendants in the ICD-10-CM classification.
The returned codes are ordered as in a pre-order depth-first traversal of the tree containing the ICD-10-CM classification.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;an ArrayList&lt;String&gt; containing the descendants of code  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getDescendants(String code)
Given a String that contains an ICD-10-CM code, it returns an ArrayList&lt;String&gt; containing all its descendants in the ICD-10-CM classification.
The returned codes are ordered as in a pre-order depth-first traversal of the tree containing the ICD-10-CM classification.
Version of `getDescendants` where the parameter `prioritizeBlocks` is implicitly false.
Please see [Blocks containing only one category](#blocks-containing-only-one-category) for the meaning of the missing parameter.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;an ArrayList&lt;String&gt; containing the descendants of code  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### boolean isAncestor(String a, String b, boolean prioritizeBlocksA, boolean prioritizeBlocksB)
It checks whether a code (`a`) is one of the ancestors of another code (`b`). A code is never an ancestor of itself.

Parameters:  
&ensp;&ensp;`a` is the code that may or may not be an ancestor of b  
&ensp;&ensp;`b` is the code that whose ancestors could include a  
&ensp;&ensp;`prioritizeBlocksA` prioritizeBlocks referred to the code in a, please see [Blocks containing only one category](#blocks-containing-only-one-category)  
&ensp;&ensp;`prioritizeBlocksB` prioritizeBlocks referred to the code in b, please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;true if a is one of the ancestors of b, false otherwise  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if a or b are not a valid ICD-10-CM code

### boolean isAncestor(String a, String b)
It checks whether a code (`a`) is one of the ancestors of another code (`b`). A code is never an ancestor of itself.
Version of `isAncestor` where the parameters `prioritizeBlocksA` and `prioritizeBlocksB` are implicitly false.
Please see [Blocks containing only one category](#blocks-containing-only-one-category) for the meaning of the missing parameters.

Parameters:  
&ensp;&ensp;`a` is the code that may or may not be an ancestor of b  
&ensp;&ensp;`b` is the code that whose ancestors could include a  
Returns:  
&ensp;&ensp;true if a is one of the ancestors of b, false otherwise  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if a or b are not a valid ICD-10-CM code

### boolean isDescendant(String a, String b, boolean prioritizeBlocksA, boolean prioritizeBlocksB)
It checks whether a code (`a`) is one of the descendants of another code (`b`). A code is never a descendant of itself.

Parameters:  
&ensp;&ensp;`a` is the code that may or may not be a descendant of b  
&ensp;&ensp;`b` is the code that whose descendants could include a  
&ensp;&ensp;`prioritizeBlocksA` prioritizeBlocks referred to the code in a, please see [Blocks containing only one category](#blocks-containing-only-one-category)  
&ensp;&ensp;`prioritizeBlocksB` prioritizeBlocks referred to the code in b, please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;true if a is one of the descendants of b, false otherwise  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if a or b are not a valid ICD-10-CM code

### boolean isDescendant(String a, String b)
It checks whether a code (`a`) is one of the descendants of another code (`b`). A code is never a descendant of itself.
Version of `isDescendant` where the parameters `prioritizeBlocksA` and `prioritizeBlocksB` are implicitly false.
Please see [Blocks containing only one category](#blocks-containing-only-one-category) for the meaning of the missing parameters.

Parameters:  
&ensp;&ensp;`a` is the code that may or may not be a descendant of b  
&ensp;&ensp;`b` is the code that whose descendants could include a  
Returns:  
&ensp;&ensp;true if a is one of the descendants of b, false otherwise  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if a or b are not a valid ICD-10-CM code

### String getNearestCommonAncestor(String a, String b, boolean prioritizeBlocksA, boolean prioritizeBlocksB)
Given two ICD-10-CM codes `a` and `b`, it returns their nearest common ancestor in the ICD-10-CM classification (or an empty string if they don't have a nearest common ancestor).

Parameters:  
&ensp;&ensp;`a` is an ICD-10-CM code  
&ensp;&ensp;`b` is an ICD-10-CM code  
&ensp;&ensp;`prioritizeBlocksA` prioritizeBlocks referred to the code in a, please see [Blocks containing only one category](#blocks-containing-only-one-category)  
&ensp;&ensp;`prioritizeBlocksB` prioritizeBlocks referred to the code in b, please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;the nearest common ancestor of a and b if it exists, an empty string otherwise  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if a or b are not a valid ICD-10-CM code

### String getNearestCommonAncestor(String a, String b)
Given two ICD-10-CM codes `a` and `b`, it returns their nearest common ancestor in the ICD-10-CM classification (or an empty string if they don't have a nearest common ancestor).
Version of `getNearestCommonAncestor` where the parameters `prioritizeBlocksA` and `prioritizeBlocksB` are implicitly false.
Please see [Blocks containing only one category](#blocks-containing-only-one-category) for the meaning of the missing parameters.

Parameters:  
&ensp;&ensp;`a` is an ICD-10-CM code  
&ensp;&ensp;`b` is an ICD-10-CM code  
Returns:  
&ensp;&ensp;the nearest common ancestor of a and b if it exists, an empty string otherwise  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if a or b are not a valid ICD-10-CM code

### boolean isLeaf(String code, boolean prioritizeBlocks)
Given a String that contains an ICD-10-CM code, it checks whether that code is a leaf in the ICD-10 classification.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
&ensp;&ensp;`prioritizeBlocks` please see [Blocks containing only one category](#blocks-containing-only-one-category)  
Returns:  
&ensp;&ensp;true if code is a leaf in the ICD-10-CM classification (that is, if it has no children), false otherwise  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### boolean isLeaf(String code)
Given a String that contains an ICD-10-CM code, it checks whether that code is a leaf in the ICD-10 classification.
Version of `isLeaf` where the parameter `prioritizeBlocks` is implicitly false.
Please see [Blocks containing only one category](#blocks-containing-only-one-category) for the meaning of the missing parameter.

Parameters:  
&ensp;&ensp;`code` is the ICD-10-CM code  
Returns:  
&ensp;&ensp;true if code is a leaf in the ICD-10-CM classification (that is, if it has no children), false otherwise  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### ArrayList&lt;String&gt; getAllCodes(boolean withDots)
It returns an ArrayList&lt;String&gt; that contains all the codes in the ICD-10-CM classification, ordered as in a depth-first pre-order visit.

Parameters:  
&ensp;&ensp;`withDots` is a boolean that controls whether the codes in the list that is returned are in the format with or without the dot.  
Returns:  
&ensp;&ensp;the list of all the codes in the ICD-10-CM classification, ordered as in a depth-first pre-order visit, in the format with the dot if withDots is true, in the format without the dot otherwise

### ArrayList&lt;String&gt; getAllCodes()
It returns an ArrayList&lt;String&gt; that contains all the codes in the ICD-10-CM classification, ordered as in a depth-first pre-order visit.

Returns:  
&ensp;&ensp;the list of all the codes in the ICD-10-CM classification, ordered as in a depth-first pre-order visit, in the format with the dot

### int getIndex(String code)
It returns the index of a particular code in the list returned by `getAllCodes`.

Parameters:  
&ensp;&ensp;`code` is the code whose index we want to find  
Returns:  
&ensp;&ensp;the index of code in the list returned by getAllCodes  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### String removeDot(String code)
Given an ICD-10-CM code, it returns the same code in the format without the dot.

Parameters:  
&ensp;&ensp;`code` is an ICD-10-CM code  
Returns:  
&ensp;&ensp;the same code in the format without the dot  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

### String addDot(String code)
Given an ICD-10-CM code, it returns the same code in the format with the dot.

Parameters:  
&ensp;&ensp;`code` is an ICD-10-CM code  
Returns:  
&ensp;&ensp;the same code in the format with the dot  
Throws:  
&ensp;&ensp;`IllegalArgumentException` if code is not a valid ICD-10-CM code

## Conclusion
This should be everything you need to know about the SimpleICD10CM-Java-edition library. Please contact me if you find any mistake, bug, missing feature or anything else that could be improved or made easier to comprehend, both in this documentation and in the library itself. You can also contact me if you need any help using this library, but I may not be able to help with questions about the ICD-10-CM classification itself. This library currently only support the January 2021 release of ICD-10-CM: let me know if you'd like me to implement the ability to switch between different versions of ICD-10-CM, and also tell me which release or releases you are interested in.

If you find this library useful and are feeling generous, consider making a donation using one of the methods listed at the end of this document.

*Stefano Travasci*

---

Paypal: [![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif)](https://www.paypal.com/donate?hosted_button_id=9HMMFAZE248VN)

Curecoin: BMq2siLdqgF1htamKcrRPedUE4CBTRsZsT

Bitcoin: bc1qea8l4xge32ylw00l4dqrdjhzw72lu3hkg9fnlq

<sub>*let me know if your favorite donation method is not in this list*</sub>
