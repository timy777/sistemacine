package sistemacine.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PeliculaMapperTest {

    private PeliculaMapper peliculaMapper;

    @BeforeEach
    public void setUp() {
        peliculaMapper = new PeliculaMapperImpl();
    }
}
