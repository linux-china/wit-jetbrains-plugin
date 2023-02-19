package org.mvnsearch.plugins.wit.lang.psi.impl;

import org.jetbrains.annotations.NotNull;
import org.mvnsearch.plugins.wit.lang.psi.WitInterfaceItem;
import org.mvnsearch.plugins.wit.lang.psi.WitInterfaceName;

public class WitPsiImplUtil {
    public static String getName(@NotNull WitInterfaceItem element) {
        final WitInterfaceName interfaceName = element.getInterfaceName();
        if (interfaceName != null) {
            return interfaceName.getText();
        } else {
            return null;
        }
    }
}
