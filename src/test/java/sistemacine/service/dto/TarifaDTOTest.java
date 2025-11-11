package sistemacine.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sistemacine.web.rest.TestUtil;

class TarifaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TarifaDTO.class);
        TarifaDTO tarifaDTO1 = new TarifaDTO();
        tarifaDTO1.setId("id1");
        TarifaDTO tarifaDTO2 = new TarifaDTO();
        assertThat(tarifaDTO1).isNotEqualTo(tarifaDTO2);
        tarifaDTO2.setId(tarifaDTO1.getId());
        assertThat(tarifaDTO1).isEqualTo(tarifaDTO2);
        tarifaDTO2.setId("id2");
        assertThat(tarifaDTO1).isNotEqualTo(tarifaDTO2);
        tarifaDTO1.setId(null);
        assertThat(tarifaDTO1).isNotEqualTo(tarifaDTO2);
    }
}
