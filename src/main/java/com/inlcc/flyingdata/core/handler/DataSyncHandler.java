package com.inlcc.flyingdata.core.handler;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public interface DataSyncHandler<S, T> {

    T handle(S s);

}
