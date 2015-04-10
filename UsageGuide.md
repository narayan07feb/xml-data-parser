#This is usage guidance page for the library.

# Introduction #

The library intends to resolve the task of parsing XML in application developments.
In order to minimize the efforts of developer the library has been created and may not fit in all condition, hence you should understand the usage well before you start using the library.


# Details #

Library uses the concepts of reflection in order to obtain its results.
There are 3 major components in which the library can be devided.
1. The interface
2. The Data classes
3. The Parser


## Interface ##
In order to consume the result of the parsing you need to implement the **ParserInterface** in the class who is responsible to handle the parsed data.

## Data Class ##
The library is unique in itself as it allows users to be free from using any Data class as it accepts the super class **Object** for processing.

In order to identify and map the variables of the class with the respective tags of xml, user has to add **annotations**.

### Anntonations ###
Library, as for now supports 2 level of nesting for XML data.

example:


&lt;root&gt;




&lt;node0 attr="hi"&gt;




&lt;node1&gt;



&lt;/node1&gt;




&lt;node2&gt;


> 

&lt;node3&gt;



&lt;/node3&gt;


> 

&lt;node4&gt;



&lt;/node4&gt;


> 

&lt;node5&gt;



&lt;/node5&gt;




&lt;/node2&gt;




&lt;node6&gt;



&lt;/node6&gt;




&lt;/node0&gt;




&lt;node0&gt;




&lt;node1&gt;



&lt;/node1&gt;




&lt;node2&gt;


> 

&lt;node3&gt;



&lt;/node3&gt;


> 

&lt;node4&gt;



&lt;/node4&gt;


> 

&lt;node5&gt;



&lt;/node5&gt;




&lt;/node2&gt;




&lt;node6&gt;



&lt;/node6&gt;




&lt;/node0&gt;




&lt;root&gt;



In order to achieve so you need to use the following Annotations.
1. **MapsToXMLElement** Use this in order to map XML tags with functions
2. **MapsToXMLAttributes** Use this when you need to retrieve the attribute value in a particular section.
3. **MapsToXMLSubMethods** This will let the library know that there are methods in this Class also which are to be used for nested data.

## Parser ##
This is the heart of the library.
Currently we are using the SAX parser, moving ahead will try to shift toward the XmlPullParser as its much more efficient than SAX parser.


So, that's it. Give it a try.
The library comes pre-bundled with a sample application.
You can see the implementation required in the Sample and can create your application with ease.
