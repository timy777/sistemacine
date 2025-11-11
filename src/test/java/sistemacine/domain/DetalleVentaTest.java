package sistemacine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sistemacine.web.rest.TestUtil;

class DetalleVentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetalleVenta.class);
        DetalleVenta detalleVenta1 = new DetalleVenta();
        detalleVenta1.setId("id1");
        DetalleVenta detalleVenta2 = new DetalleVenta();
        detalleVenta2.setId(detalleVenta1.getId());
        assertThat(detalleVenta1).isEqualTo(detalleVenta2);
        detalleVenta2.setId("id2");
        assertThat(detalleVenta1).isNotEqualTo(detalleVenta2);
        detalleVenta1.setId(null);
        assertThat(detalleVenta1).isNotEqualTo(detalleVenta2);
    }
}
