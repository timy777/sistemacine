package sistemacine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sistemacine.web.rest.TestUtil;

class FuncionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Funcion.class);
        Funcion funcion1 = new Funcion();
        funcion1.setId(1L);
        Funcion funcion2 = new Funcion();
        funcion2.setId(funcion1.getId());
        assertThat(funcion1).isEqualTo(funcion2);
        funcion2.setId(2L);
        assertThat(funcion1).isNotEqualTo(funcion2);
        funcion1.setId(null);
        assertThat(funcion1).isNotEqualTo(funcion2);
    }
}
