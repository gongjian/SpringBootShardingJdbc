package com.ebao.SpringBootShardingJdbc.configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
//import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.ebao.SpringBootShardingJdbc.algorithm.ModuloDatabaseShardingAlgorithm;
//import com.ebao.SpringBootShardingJdbc.algorithm.ModuloTableShardingAlgorithm;

@Configuration
public class DataSourceConfiguration {

	@Bean
	public DataSource getDataSource() {
		return buildDataSource();
	}

	private DataSource buildDataSource() {
		
		Map<String, DataSource> dsMap = new HashMap<>(2);
		
		//添加数据源
		dsMap.put("dbtbl_0", createDataSource("dbtbl_0"));
		dsMap.put("dbtbl_1", createDataSource("dbtbl_1"));
		
		//构建数据源的分布规则,dbtbl_0是默认数据源
		DataSourceRule dataSourceRule = new DataSourceRule(dsMap, "dbtbl_0");
		
		//设置分表映射
		/*TableRule orderTableRule = TableRule.builder("t_order")
				.actualTables(Arrays.asList("t_order_0", "t_order_1", "t_order_2", "t_order_3"))
				.dataSourceRule(dataSourceRule)
				.build();*/
		TableRule orderTableRule = TableRule.builder("t_order")
				.actualTables(Arrays.asList("t_order_0"))
				.dataSourceRule(dataSourceRule)
				.build();
		
		//具体分表策略
		ShardingRule shardingRule = ShardingRule.builder()
				.dataSourceRule(dataSourceRule)
				.tableRules(Arrays.asList(orderTableRule))
				.databaseShardingStrategy(new DatabaseShardingStrategy("order_id", new ModuloDatabaseShardingAlgorithm()))
                //.tableShardingStrategy(new TableShardingStrategy("order_id", new ModuloTableShardingAlgorithm()))
                .build();
		
		DataSource dataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
		
		return dataSource;
	}

	private DataSource createDataSource(final String dataSourceName) {
		DruidDataSource ds = new DruidDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl(String.format("jdbc:mysql://localhost:3306/%s", dataSourceName));
		ds.setUsername("root");
		ds.setPassword("123456");
		
		return ds;
	}

}
