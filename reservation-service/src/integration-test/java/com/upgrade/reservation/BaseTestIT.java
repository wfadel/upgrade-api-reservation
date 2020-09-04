package com.upgrade.reservation;

import com.upgrade.reservation.config.TestConfiguration;
import com.upgrade.reservation.util.DbUtil;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@TestPropertySource(locations = "classpath:application-integration-test.properties")
public abstract class BaseTestIT {

    @Autowired
    private DbUtil dbUtil;

    @Before
    public void resetTables() {
        dbUtil.resetTables();
    }

}
