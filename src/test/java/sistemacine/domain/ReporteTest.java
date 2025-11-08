package sistemacine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sistemacine.web.rest.TestUtil;

class ReporteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reporte.class);
        Reporte reporte1 = new Reporte();
        reporte1.setId(1L);
        Reporte reporte2 = new Reporte();
        reporte2.setId(reporte1.getId());
        assertThat(reporte1).isEqualTo(reporte2);
        reporte2.setId(2L);
        assertThat(reporte1).isNotEqualTo(reporte2);
        reporte1.setId(null);
        assertThat(reporte1).isNotEqualTo(reporte2);
    }
}
