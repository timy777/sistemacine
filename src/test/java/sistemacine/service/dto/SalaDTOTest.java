package sistemacine.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sistemacine.web.rest.TestUtil;

class SalaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaDTO.class);
        SalaDTO salaDTO1 = new SalaDTO();
        salaDTO1.setId(1L);
        SalaDTO salaDTO2 = new SalaDTO();
        assertThat(salaDTO1).isNotEqualTo(salaDTO2);
        salaDTO2.setId(salaDTO1.getId());
        assertThat(salaDTO1).isEqualTo(salaDTO2);
        salaDTO2.setId(2L);
        assertThat(salaDTO1).isNotEqualTo(salaDTO2);
        salaDTO1.setId(null);
        assertThat(salaDTO1).isNotEqualTo(salaDTO2);
    }
}
