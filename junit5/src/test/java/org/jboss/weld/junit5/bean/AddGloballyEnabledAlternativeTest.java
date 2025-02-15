package org.jboss.weld.junit5.bean;

import org.jboss.weld.junit.MockBean;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Isolated;
import org.mockito.Mockito;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.TypeLiteral;

import java.util.List;
import java.util.Set;

/**
 * Tests {@link org.jboss.weld.junit.MockBean} adding a bean that is a globally enabled alternative.
 *
 * @author Matej Novotny
 */
@Isolated
@ExtendWith(WeldJunit5Extension.class)
public class AddGloballyEnabledAlternativeTest {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(Bar.class)
            .addBeans(createFooAlternativeBean(), createListBean())
            .build();

    public static Bean<?> createFooAlternativeBean(){
        return MockBean.read(Foo.class)
                .priority(3)
                .alternative(true)
                .addQualifier(Meaty.Literal.INSTANCE)
                .build();
    }

    static Bean<?> createListBean() {
        return MockBean.builder()
                .types(new TypeLiteral<List<String>>() {
                }.getType())
                .globallySelectedAlternative(2)
                .creating(
                        // Mock object provided by Mockito
                        Mockito.when(Mockito.mock(List.class).get(0)).thenReturn("42").getMock())
                .build();
    }

    @Test
    public void testAllBeansAreAddedAndCanBeSelected() {
        Bar bar = weld.select(Bar.class).get();
        Assertions.assertNotNull(bar.getFoo());
        Assertions.assertNotNull(bar.getSomeList());
        Assertions.assertEquals("42", bar.getSomeList().get(0));

        // assert all of these are actually alternatives (enabled)
        Set<Bean<?>> beans = weld.getBeanManager().getBeans(Foo.class, Meaty.Literal.INSTANCE);
        Assertions.assertEquals(1, beans.size());
        Assertions.assertTrue(beans.iterator().next().isAlternative());

        beans = weld.getBeanManager().getBeans(new TypeLiteral<List<String>>(){}.getType());
        Assertions.assertEquals(1, beans.size());
        Assertions.assertTrue(beans.iterator().next().isAlternative());
    }
}
