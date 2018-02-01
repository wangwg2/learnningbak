package com.dsl.gradle;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author wangwg2
 */
public class MessageServiceTest {
  private MessageService messageService;

  @Before
  public void setUp() {
    messageService = new MessageService();
  }

  @Test
  public void getMessage_ShouldReturnMessage() {
    assertEquals("Hello World!", messageService.getMessage());
  }
}
