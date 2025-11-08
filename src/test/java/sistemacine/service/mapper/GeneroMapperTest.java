package sistemacine.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeneroMapperTest {

    private GeneroMapper generoMapper;

    @BeforeEach
    public void setUp() {
        generoMapper = new GeneroMapperImpl();
    }
}
