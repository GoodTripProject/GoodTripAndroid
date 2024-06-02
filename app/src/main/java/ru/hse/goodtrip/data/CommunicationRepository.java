package ru.hse.goodtrip.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import retrofit2.Call;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.ResultHolder;
import ru.hse.goodtrip.network.NetworkManager;
import ru.hse.goodtrip.network.social.CommunicationService;
import ru.hse.goodtrip.network.social.entities.User;

/**
 * Repository for working with communication data.
 */
public class CommunicationRepository extends AbstractRepository {

  private static volatile CommunicationRepository instance;
  private final CommunicationService communicationService;

  private CommunicationRepository() {
    communicationService = NetworkManager.getInstance()
        .getInstanceOfService(CommunicationService.class);
  }

  /**
   * Set like to the trip.
   *
   * @param userId id of user who is setting like.
   * @param tripId id of trip which liked
   * @param token  Jwt token.
   */
  public void setLike(int userId, int tripId, String token) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> setLikeCall = communicationService.like(userId, tripId, getWrappedToken(token));
    setLikeCall.enqueue(getCallback(resultHolder,
        "Cannot set like to this post", (result) -> {
        }));
  }

  /**
   * Delete like from the trip.
   *
   * @param userId id of user who is setting like.
   * @param tripId id of trip which liked
   * @param token  Jwt token.
   */
  public void deleteLike(int userId, int tripId, String token) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> deleteLikeCall = communicationService.delete_like(userId, tripId,
        getWrappedToken(token));
    deleteLikeCall.enqueue(getCallback(resultHolder,
        "Cannot delete like from this post", (result) -> {
        }));
  }

  /**
   * Follow user by handle.
   *
   * @param userId id of user.
   * @param handle handle of author who we want to follow
   * @param token  Jwt token.
   * @return Completable Future of Result String.
   */
  public CompletableFuture<Result<String>> follow(int userId, String handle, String token) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> followCall = communicationService.follow(userId, handle, getWrappedToken(token));
    followCall.enqueue(getCallback(resultHolder,
        "Cannot follow user", (result) -> {
        }));
    return getCompletableFuture(resultHolder);
  }

  /**
   * Unfollow user by handle.
   *
   * @param userId id of user.
   * @param handle handle of author who we want to follow
   * @param token  Jwt token.
   * @return Completable Future of Result String.
   */
  public CompletableFuture<Result<String>> unfollow(int userId, String handle, String token) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> unfollowCall = communicationService.unfollow(userId, handle,
        getWrappedToken(token));
    unfollowCall.enqueue(getCallback(resultHolder,
        "Cannot unfollow user", (result) -> {
        }));
    return getCompletableFuture(resultHolder);
  }

  /**
   * Get followers of user.
   *
   * @param userId id of user.
   * @param token  Jwt token.
   * @return Completable Future of Result String.
   */
  public CompletableFuture<Result<List<User>>> getFollowers(int userId, String token) {
    ResultHolder<List<User>> resultHolder = new ResultHolder<>();
    Call<List<User>> getFollowersCall = communicationService.getFollowers(userId,
        getWrappedToken(token));
    getFollowersCall.enqueue(getCallback(resultHolder,
        "Cannot get followers", (result) -> {
        }));
    return getCompletableFuture(resultHolder);
  }

  /**
   * Get subscriptions of user.
   *
   * @param userId id of user.
   * @param token  Jwt token.
   * @return Completable Future of Result List of User.
   */
  public CompletableFuture<Result<List<User>>> getSubscriptions(int userId, String token) {
    ResultHolder<List<User>> resultHolder = new ResultHolder<>();
    Call<List<User>> getSubscriptions = communicationService.getSubscriptions(userId,
        getWrappedToken(token));
    getSubscriptions.enqueue(getCallback(resultHolder,
        "Cannot get subscription", (result) -> {
        }));
    return getCompletableFuture(resultHolder);
  }

  public CompletableFuture<Result<User>> getUserByHandle(String handle, String token){
    ResultHolder<User> resultHolder = new ResultHolder<>();
    Call<User> getUserByHandle = communicationService.getUserByHandle(handle,
        getWrappedToken(token));
    getUserByHandle.enqueue(getCallback(resultHolder,
        "Cannot get user by handle", (result) -> {
        }));
    return getCompletableFuture(resultHolder);
  }
  public static CommunicationRepository getInstance() {
    if (instance == null) {
      instance = new CommunicationRepository();
    }
    return instance;
  }
}
