package edu.byu.cs.tweeter.server.factory;

import edu.byu.cs.tweeter.server.dao.FollowDAOInterface;
import edu.byu.cs.tweeter.server.dao.StatusDAOInterface;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;

public interface FactoryInterface {
    FollowDAOInterface getFollowDAO();
    UserDAOInterface getUserDAO();
    StatusDAOInterface getStatusDAO();
}
