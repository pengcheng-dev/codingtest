# 1. Simulating 1 Billion EntryTransaction Records

## Overview
To simulate 1 billion `EntryTransaction` records, I will use database sharding and parallel writing techniques. The goal is to efficiently manage and insert a vast amount of data into a MySQL database while maintaining performance and scalability.

## Strategy

### Sharding
To handle the large dataset like 1 billion records, I will implement sharding, which involves distributing the data across multiple database instances.

#### Sharding Key
- I will use **hash-based sharding** on the `taccId` (account ID), which is expected to distribute the data evenly across the shards.

### Parallel Writing
To insert data into the shards efficiently, I will use parallel writing:

- **ExecutorService**: Utilize Java’s `ExecutorService` to manage a pool of threads (same with CPU cores), each responsible for inserting a portion of the data into the database.
- **Batch Processing**: Each thread will execute batch inserts to minimize database write overhead.

## Considerations

- **Performance**: Consider disable foreign key restraints before inserting to improve performance.


# 2. Database Schema Changes: remove the `year` attribute from `TAccountTags`

## Overview
To remove the `year` attribute from the `TAccountTags` table in a production environment, it's better to rename a duplicate table than drop column directly to shorter system downtime.

### 1. Create a Duplicate of the Table
Create a new table without the unwanted column and ensure it has the same schema except for the removed `year`:
```sql
CREATE TABLE TAccountTags_new LIKE TAccountTags;
ALTER TABLE TAccountTags_new DROP COLUMN year;
```
### 2. Synchronize Existing Data to new table
Copy the existing data from the old table to the new table:
```sql
INSERT INTO TAccountTags_new SELECT * FROM TAccountTags;
```
### 3. Stop requests from the client in a maintenance window
Once duplicate is done, need to stop receiving client requests.

### 4. Switch to the New Table
Rename the tables to switch to the new table:
```sql
RENAME TABLE TAccountTags TO TAccountTags_old, TAccountTags_new TO TAccountTags;
```

### 5. Handle Incremental Data
Synchronize any changes of the original table to the new table before blocking client requests by a customized tool.

### 6. Re-establish Constraints and Triggers
Update constraint to ensure `TAccountTag` reference the new table structure.

### 7. Backup and Cleanup
Backup and remove the old table:
```sql
DROP TABLE TAccountTags_old;
```

## Considerations

- **Downtime**: This approach aims to minimize downtime by performing most operations while the system is live and only requiring a brief switch period.