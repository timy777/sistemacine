package sistemacine.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sistemacine.web.rest.TestUtil;

class ReporteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReporteDTO.class);
        ReporteDTO reporteDTO1 = new ReporteDTO();
        reporteDTO1.setId("id1");
        ReporteDTO reporteDTO2 = new ReporteDTO();
        assertThat(reporteDTO1).isNotEqualTo(reporteDTO2);
        reporteDTO2.setId(reporteDTO1.getId());
        assertThat(reporteDTO1).isEqualTo(reporteDTO2);
        reporteDTO2.setId("id2");
        assertThat(reporteDTO1).isNotEqualTo(reporteDTO2);
        reporteDTO1.setId(null);
        assertThat(reporteDTO1).isNotEqualTo(reporteDTO2);
    }
}
