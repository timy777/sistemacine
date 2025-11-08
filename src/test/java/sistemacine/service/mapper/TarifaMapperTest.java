package sistemacine.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TarifaMapperTest {

    private TarifaMapper tarifaMapper;

    @BeforeEach
    public void setUp() {
        tarifaMapper = new TarifaMapperImpl();
    }
}
