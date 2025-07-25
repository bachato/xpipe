package io.xpipe.ext.system.incus;

import io.xpipe.app.action.AbstractAction;
import io.xpipe.app.core.AppI18n;
import io.xpipe.app.hub.action.HubLeafProvider;
import io.xpipe.app.hub.action.StoreAction;
import io.xpipe.app.storage.DataStoreEntryRef;
import io.xpipe.app.terminal.TerminalLauncher;
import io.xpipe.app.util.LabelGraphic;

import javafx.beans.value.ObservableValue;

import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

public class IncusContainerEditConfigActionProvider implements HubLeafProvider<IncusContainerStore> {

    @Override
    public AbstractAction createAction(DataStoreEntryRef<IncusContainerStore> ref) {
        return Action.builder().ref(ref).build();
    }

    @Override
    public Class<IncusContainerStore> getApplicableClass() {
        return IncusContainerStore.class;
    }

    @Override
    public ObservableValue<String> getName(DataStoreEntryRef<IncusContainerStore> store) {
        return AppI18n.observable("editConfiguration");
    }

    @Override
    public LabelGraphic getIcon(DataStoreEntryRef<IncusContainerStore> store) {
        return new LabelGraphic.IconGraphic("mdi2f-file-document-edit");
    }

    @Override
    public boolean requiresValidStore() {
        return false;
    }

    @Override
    public String getId() {
        return "editIncusContainerConfig";
    }

    @Jacksonized
    @SuperBuilder
    public static class Action extends StoreAction<IncusContainerStore> {

        @Override
        public boolean isMutation() {
            return true;
        }

        @Override
        public void executeImpl() throws Exception {
            var d = (IncusContainerStore) ref.getStore();
            var view = new IncusCommandView(
                    d.getInstall().getStore().getHost().getStore().getOrStartSession());
            TerminalLauncher.open(ref.get().getName(), view.configEdit(d.getContainerName()));
        }
    }
}
