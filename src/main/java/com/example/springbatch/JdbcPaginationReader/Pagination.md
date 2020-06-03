In order for paging to work, you need to be able to query based on a page size and page number
(the number of records to return and which page you are currently processing). For example, if your total number of records is 10,000 and your page size is 100 records, you need to be able to specify that you
are requesting the 20th page of 100 records (or records 2,000 through 2100). To do this, you provide an implementation of the org.springframework.batch.item.database.PagingQueryProvider interface to the JdbcPagingItemReader.

The PagingQueryProvider interface provides all of the functionality required to navigate a paged ResultSet.
Unfortunately, each database offers its own paging implementation. Because of this, you have the following two options:
1. Configure a database-specific implementation of the PagingQueryProvider. As of this writing, Spring Batch provides implementations for DB2, Derby, H2, HSql, MySQL, Oracle, Postgres, SqlServer, and Sybase.
2. Configure your reader to use the org.springframework.batch.item.database. support.SqlPagingQueryProviderFactoryBean. This factory detects what database implementation to use.