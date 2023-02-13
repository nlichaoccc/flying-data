package com.inlcc.flyingdata.core.handler;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public class PrintDataSyncHandler implements DataSyncHandler<User, User> {

    @Override
    public User handle(User user) {
        System.out.println(user);
        return user;
    }
}
