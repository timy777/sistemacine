package sistemacine.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DetalleVentaMapperTest {

    private DetalleVentaMapper detalleVentaMapper;

    @BeforeEach
    public void setUp() {
        detalleVentaMapper = new DetalleVentaMapperImpl();
    }
}
