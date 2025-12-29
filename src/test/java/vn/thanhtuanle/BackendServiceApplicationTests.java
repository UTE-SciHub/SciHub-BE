package vn.thanhtuanle;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import vn.thanhtuanle.common.service.CloudinaryService;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
class BackendServiceApplicationTests {

    @MockBean
    private CloudinaryService cloudinaryService;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }

    @Test
    void cloudinaryServiceIsMocked() {
        assertThat(cloudinaryService).isNotNull();
    }
    
    @Test
    void mainMethodStartsApplication() {
        try {
            BackendServiceApplication.main(new String[]{"--spring.profiles.active=test"});
        } catch (Exception e) {
            assertThat(e).isNull();
        }
    }
}
