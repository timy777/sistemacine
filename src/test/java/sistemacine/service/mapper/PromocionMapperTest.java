package sistemacine.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PromocionMapperTest {

    private PromocionMapper promocionMapper;

    @BeforeEach
    public void setUp() {
        promocionMapper = new PromocionMapperImpl();
    }
}
