package com.example.grouperapi;

import com.example.grouperapi.init.DBInit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
class GrouperApiApplicationTests {
	@MockBean
	DBInit dbInit;

	@Test
	void contextLoads() {
	}

}
