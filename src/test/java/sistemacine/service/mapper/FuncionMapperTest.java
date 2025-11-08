package sistemacine.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FuncionMapperTest {

    private FuncionMapper funcionMapper;

    @BeforeEach
    public void setUp() {
        funcionMapper = new FuncionMapperImpl();
    }
}
