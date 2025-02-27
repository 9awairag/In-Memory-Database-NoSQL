package com.amolw2905.command;

import com.amolw2905.exception.KeyNotFoundException;

public interface IDatabaseCommands {

    Object execute (Object object) throws KeyNotFoundException;

    Object undo() throws KeyNotFoundException;
}
