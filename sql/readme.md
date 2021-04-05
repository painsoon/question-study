### 逻辑执行顺序  

```
(6)SELECT [DISTINCT | ALL] col1, col2, agg_func(col3) AS alias
(1)  FROM t1 JOIN t2
(2)    ON (join_conditions)
(3) WHERE where_conditions
(4) GROUP BY col1, col2
(5)HAVING having_condition
(7) UNION [ALL]
   ...
(8) ORDER BY col1 ASC,col2 DESC
(9)OFFSET m ROWS FETCH NEXT num_rows ROWS ONLY;
```

1. 首先，FROM 和 JOIN 是 SQL 语句执行的第一步。它们的逻辑结果是一个笛卡尔积，决定了接下来要操作的数据集。注意逻辑执行顺序并不代表物理执行顺序，实际上数据库在获取表中的数据之前会使用 ON 和 WHERE 过滤条件进行优化访问；  
2. 其次，应用 ON 条件对上一步的结果进行过滤并生成新的数据集；  
3. 然后，执行 WHERE 子句对上一步的数据集再次进行过滤。WHERE 和 ON 大多数情况下的效果相同，但是外连接查询有所区别.   
4. 接着，基于 GROUP BY 子句指定的表达式进行分组；同时，对于每个分组计算聚合函数 agg_func 的结果。经过 GROUP BY 处理之后，数据集的结构就发生了变化，只保留了分组字段和聚合函数的结果；  
5. 如果存在 GROUP BY 子句，可以利用 HAVING 针对分组后的结果进一步进行过滤，通常是针对聚合函数的结果进行过滤；   
6. 接下来，SELECT 可以指定要返回的列；如果指定了 DISTINCT 关键字，需要对结果集进行去重操作。另外还会为指定了 AS 的字段生成别名；  
7. 如果还有集合操作符（UNION、INTERSECT、EXCEPT）和其他的 SELECT 语句，执行该查询并且合并两个结果集。对于集合操作中的多个 SELECT 语句，数据库通常可以支持并发执行；   
8. 然后，应用 ORDER BY 子句对结果进行排序。如果存在 GROUP BY 子句或者 DISTINCT 关键字，只能使用分组字段和聚合函数进行排序；否则，可以使用 FROM 和 JOIN 表中的任何字段排序；  
9. 最后，OFFSET 和 FETCH（LIMIT、TOP）限定了最终返回的行数。  


了解 SQL 逻辑执行顺序可以帮助我们进行 SQL 优化。例如 WHERE 子句在 HAVING 子句之前执行，因此我们应该尽量使用 WHERE 进行数据过滤，避免无谓的操作；除非业务需要针对聚合函数的结果进行过滤。  


### 如何建索引  

where: 经常出现在where字段简历索引避免全表扫描；  

order： 排序字段加入索引，避免额外的排序操作；  

join： 多表连接关键字段建立，提高连接查询性能；

group by： 利用索引完成分组；

### 索引失效问题  

在where中对索引字段进行表达式运算或者使用函数都会导致索引失效，这种情况包括字段的数据的字段类型不匹配。  

使用like匹配时，通配符出现在左侧无法使用索引。

如果where条件字段创建了索引你，尽量设置not null，不是所有数据库用is not null都可以利用索引。  

### explain 执行计划  

是数据库执行 SQL 语句的具体步骤，例如通过索引还是全表扫描访问表中的数据，连接查询的实现方式和连接的顺序等。  

### 尽量避免子查询   

将子查询替换为等价的 JOIN 语句，利用物化技术将子查询的结果生成一个内存临时表然后连接。   
### 不要使用offset实现分页  

**分页查询的原理是先跳过指定行数，再返回top-n记录**  

LIMIT 10 OFFSET N;
查询随着 OFFSET 的增加，速度会越来越慢；因为即使我们只需要返回 10 条记录，数据库仍然需要访问并且过滤掉 N（比如 1000000）行记录，即使通过索引也会涉及不必要的扫描操作。

对于以上分页查询，更好的方法是记住上一次获取到的最大 id，然后在下一次查询中作为条件传入：  
```
 WHERE id > last_id
 ORDER BY id
 LIMIT 10;  
```
如果 id 字段上存在索引，这种分页查询的方式可以基本不受数据量的影响。

