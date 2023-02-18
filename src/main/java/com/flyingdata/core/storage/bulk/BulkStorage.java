package com.flyingdata.core.storage.bulk;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/18
 */
public interface BulkStorage {

    void resetBulk();

    BulkStorage add(InsertStorage insertStorage);

    BulkStorage add(UpdateStorage updateStorage);

    BulkStorage add(DeleteStorage deleteStorage);

    BulkResponse bulk();

    interface InsertStorage {
    }

    interface UpdateStorage {
    }

    interface DeleteStorage {
    }

    interface BulkResponse {
    }

}
