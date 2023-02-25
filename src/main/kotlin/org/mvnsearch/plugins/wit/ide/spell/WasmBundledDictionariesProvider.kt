package org.mvnsearch.plugins.wit.ide.spell

import com.intellij.spellchecker.BundledDictionaryProvider

class WasmBundledDictionariesProvider : BundledDictionaryProvider {
    override fun getBundledDictionaries(): Array<String> {
        return arrayOf("wasm.dic")
    }
}