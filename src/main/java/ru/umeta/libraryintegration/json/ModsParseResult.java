package ru.umeta.libraryintegration.json;

import gov.loc.mods.v3.ModsDefinition;

/**
 * Created by k.kosolapov on 14/04/2015.
 */
public class ModsParseResult extends ParseResult {
    private ModsDefinition modsDefinition;

    public ModsParseResult(String title, String isbn, String author, ModsDefinition modsDefinition) {
        super(title, isbn, author);
        this.modsDefinition = modsDefinition;
    }

    public ModsDefinition getModsDefinition() {
        return modsDefinition;
    }

    public void setModsDefinition(ModsDefinition modsDefinition) {
        this.modsDefinition = modsDefinition;
    }

    @Override
    public ParseResult clone() {
        return new ModsParseResult(title, isbn, author, modsDefinition);
    }
}
