In Spring Batch, you use a StAX parser

 When working with XML, Spring Batch parses XML fragments that you define into your domain objects.

  XML fragment is a block of XML from open to close tag. Each time the specified fragment exists in your file, it will be considered a single record and converted into an item to be processed.

  To parse your XML input file, you will use the org.springframework.batch.item.xml. StaxEventItemReader that Spring Batch provides. To use it, you define a fragment root element name, which identifies the root element of each fragment considered an item in your XML. In your case, this will
  be the customer tag.

   Finally, it takes an org.springframework.oxm.Unmarshaller implementation. This will be used to convert the XML to your domain object.