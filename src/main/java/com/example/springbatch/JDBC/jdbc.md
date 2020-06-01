The JdbcTemplate loops through the entire ResultSet, mapping every row to the required domain object in memory.

Spring Batch provides two different methods for loading records one at a time as they are processed: a **cursor** and **paging**. A cursor is implemented via a standard java.sql.ResultSet. When
a ResultSet is opened, every time the next() method is called a batch of records from the database is returned. This allows records to be streamed from the database on demand, which is the behavior that you need for a cursor.

Cursor returns 1 single row at a time
Paging returns 10 rows at a time

**Paging,** on the other hand, takes a bit more work. The concept of paging is that you retrieve records from the database in chunks called pages. Each page is created by its own, independent SQL query. As you read through each page, a new page is read from the database via a new query

To implement a JDBC reader (either cursor-based or page-based), you will need to do two things: configure the reader to execute the query that is required and create a RowMapper implementation just like the Spring JdbcTemplate requires to map your ResultSet to your domain object.

 RowMapper is exactly what it sounds like. It takes a row from a ResultSet and maps the fields to a domain object.I