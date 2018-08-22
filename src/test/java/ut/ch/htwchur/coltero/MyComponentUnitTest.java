package ut.ch.htwchur.coltero;

import org.junit.Test;

import ch.htwchur.coltero.api.MyPluginComponent;
import ch.htwchur.coltero.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}