**LineMapper** - This class will take each line of a file as a String and convert it into a domain object (item) to be processed.

The record will first be tokenized by the LineTokenizer into a FieldSet. From there, the FieldSet will be mapped into your domain object by the FieldSetMapper

**DefaultLineMapper** - parsing the line into a FieldSet and then mapping the fields of the FieldSet to a domain object