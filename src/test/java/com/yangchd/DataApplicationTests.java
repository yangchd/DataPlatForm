package com.yangchd;

import com.yangchd.data.dao.DataSourceDao;
import com.yangchd.data.table.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataApplicationTests {

	@Autowired
	private DataSourceDao dataSourceDao;

	@Test
	public void contextLoads() {
		DataSource dataSource = new DataSource();
		dataSource.setName("名字测试");

		dataSourceDao.save(dataSource);

		System.out.println(dataSourceDao.findAll());
	}

}
