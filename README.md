# tags #
The project provides a simple java library for writing xml / xhtml data. The interesting part of the project is the simple api, which allows to write xml / xhtml data without closing tags.

## Simple Example ##
The following code snipet:
```java
final TagBuilder tagBuilder = new TagBuilder();
final String xml = tagBuilder.createRootTag("hello").content("world").finish();
```
creates the xml output:
```xml
<hello>world</hello>
```

## The TagBuilder class ##
The `TagBuilder` class is used to set the configuration of the build process and to create a xml / xhtml root element. 

* It can be used to set the xml / xhtml declaration.
```java
final TagBuilder tagBuilder = new TagBuilder().setDeclaration("<!DOCTYPE html>");
```
* It can be used to set the number of indentation characters. The default is 0, which disables indentation. The characters are whitespaces.

* It can be used to set anescape flag. This is used as a default value, if no explicite escape flag is given. The default is `false`.

* It can be used to set a line separator, other than the system line separator.

## The Tag class ##
The `Tag` class is the main class for writing the xml / xhtml data. The first thing is to create a root tag:
```java
final TagBuilder tagBuilder = new TagBuilder();
final Tag rootTag = tagBuilder.createRootTag("root");
```

With a Tag you can create attributes, with an explicite escape flag for the attribute value or the default escape flag:
```java
final Tag rootTag = tagBuilder.createRootTag("root");
rootTag.attr("key2", "value2", true);
rootTag.attr("key1", "value1");
```

With a Tag you can create child tags:
```java
final Tag rootTag = tagBuilder.createRootTag("root").setIndentSize(2);
rootTag.createChild("root-child-1").createChild("root-child-1-child");
final String xml = rootTag.createChild("root-child-2").finish;
```
This will produce the following xml:
```xml
<root>
  <root-child-1>
    <root-child-1-child/>
  </root-child-1>
  <root-child-2/>
</root>
```

With a Tag you can add tag content, with an explicite escape flag or the default:
```java
final Tag rootTag = tagBuilder.createRootTag("root");
rootTag.content("content-1", true);
rootTag.content("content-2");
```

## Complex Example ##
The project contains a complex example, how to use the library: [TableTemplateExample](https://github.com/dead-end/tags/blob/master/src/test/java/de/dead/end/tags/TableTemplateExample.java). 
