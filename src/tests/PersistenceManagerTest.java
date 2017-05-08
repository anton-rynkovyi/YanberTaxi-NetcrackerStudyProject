import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.persistence.converter.impl.ConverterFactory;
import com.netcracker.project.study.persistence.PersistenceEntity;
import com.netcracker.project.study.persistence.manager.impl.PersistenceManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml"})
public class PersistenceManagerTest {
    @Autowired
    private PersistenceManager persistenceManager;
    @Autowired
    private ConverterFactory converterFactory;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void create() throws Exception {
        Driver driver = new Driver();
        driver.setEmail("abc.com");
        driver.setFirstName("Misha");
        driver.setName("Driver Miha");
        PersistenceEntity persistenceEntity = converterFactory.convertToEntity(driver);
        persistenceManager.create(persistenceEntity);
        // todo проверить айди не 0
    }

    @Test
    public void update() throws Exception {
        Driver driver = new Driver();
        driver.setObjectId(2);
        driver.setEmail("aaa.com");
        driver.setFirstName("Masha");
        driver.setName("Driver Masha");
        PersistenceEntity persistenceEntity = converterFactory.convertToEntity(driver);
        persistenceManager.update(persistenceEntity);
    }

    @Test
    public void delete() throws Exception {
        Driver driver = new Driver();
        driver.setObjectId(1);
        PersistenceEntity persistenceEntity = converterFactory.convertToEntity(driver);
        persistenceManager.delete(persistenceEntity);
    }

    @Test
    public void getOne() throws Exception {
        persistenceManager.getOne(2);
    }

    @Test
    public void getAll() throws Exception {
        List<PersistenceEntity> persistenceEntityList = persistenceManager.getAll(1);

    }

}
