package sistemacine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sistemacine.web.rest.TestUtil;

class TarifaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tarifa.class);
        Tarifa tarifa1 = new Tarifa();
        tarifa1.setId("id1");
        Tarifa tarifa2 = new Tarifa();
        tarifa2.setId(tarifa1.getId());
        assertThat(tarifa1).isEqualTo(tarifa2);
        tarifa2.setId("id2");
        assertThat(tarifa1).isNotEqualTo(tarifa2);
        tarifa1.setId(null);
        assertThat(tarifa1).isNotEqualTo(tarifa2);
    }
}
