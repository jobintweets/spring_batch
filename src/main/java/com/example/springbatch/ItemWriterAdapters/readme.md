As you would expect, calling an existing service with the item you’ve processed in your step is made easy with Spring Batch. However, what if your service doesn’t take the same object you’re processing? If you want to be able to extract values out of your item and pass them to your service, Spring Batch has you covered. PropertyExtractingDelegatingItemWriter

instead of blindly passing the item being processed by the step, PropertyExtractingDelegatingItemWriter
passes only the attributes of the item that are requested. For example, if you have an item of type
Product that contains fields for a database id, name, price, and SKU number, you’re required
to pass the entire Product object to the service method as with ItemWriterAdapter.

But with PropertyExtractingDelegatingItemWriter, you can specify that you only want the database id and price to be passed as parameters to the service.