package sistemacine.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sistemacine.web.rest.TestUtil;

class DetalleVentaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetalleVentaDTO.class);
        DetalleVentaDTO detalleVentaDTO1 = new DetalleVentaDTO();
        detalleVentaDTO1.setId(1L);
        DetalleVentaDTO detalleVentaDTO2 = new DetalleVentaDTO();
        assertThat(detalleVentaDTO1).isNotEqualTo(detalleVentaDTO2);
        detalleVentaDTO2.setId(detalleVentaDTO1.getId());
        assertThat(detalleVentaDTO1).isEqualTo(detalleVentaDTO2);
        detalleVentaDTO2.setId(2L);
        assertThat(detalleVentaDTO1).isNotEqualTo(detalleVentaDTO2);
        detalleVentaDTO1.setId(null);
        assertThat(detalleVentaDTO1).isNotEqualTo(detalleVentaDTO2);
    }
}
