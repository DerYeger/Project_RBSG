package de.uniks.se19.team_g.project_rbsg;

import io.rincl.*;
import io.rincl.resourcebundle.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectRbsgApplicationTests {

	@Before
	public void initRincled() {
		Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
	}

	@Test
	public void contextLoads() {
	}

}
