package com.aguadelamiseria.customtab;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestCommandListener {

    @Mock
    private CustomTab customTab;

    @Mock
    private Player player;

    @InjectMocks
    private CommandListener listener;

    @InjectMocks
    private PlayerCommandPreprocessEvent event;

    @BeforeEach
    void setup(){
        when(customTab.isWhitelist()).thenReturn(true);
        when(customTab.getCommandList(any())).thenReturn(Collections.singletonList("c-m_d"));
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Command Whitelist Test")
    public void testCommandListener() {
        event.setMessage("/c-m_d2 -/arg");
        listener.onCommandPreprocessed(event);

        assert event.isCancelled();

        event.setCancelled(false);

        event.setMessage("/c-m_d -/arg");
        listener.onCommandPreprocessed(event);

        assert !event.isCancelled();
    }
}