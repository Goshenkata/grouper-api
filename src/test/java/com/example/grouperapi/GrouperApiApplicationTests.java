package com.example.grouperapi;

import com.example.grouperapi.init.DBInit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class GrouperApiApplicationTests {
	@MockBean
	DBInit dbInit;

	@Test
	void contextLoads() {
	}

}
