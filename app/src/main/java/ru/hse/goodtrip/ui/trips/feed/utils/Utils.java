package ru.hse.goodtrip.ui.trips.feed.utils;

import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utils class.
 */
public class Utils {

  /**
   * Load image into imageView by URL and load defaultImageId if errors occurred
   *
   * @param imageView      where photo should be displayed
   * @param photoUrl       photo url
   * @param defaultImageId image that displays if error occurred (or photoUrl is null)
   */
  public static void setImageByUrl(ImageView imageView, @Nullable String photoUrl,
      int defaultImageId) {
    if (photoUrl != null && !photoUrl.trim().isEmpty()) {
      Glide.with(imageView.getContext())
          .load(photoUrl)
          .error(defaultImageId)
          .into(imageView);
    } else {
      Glide.with(imageView.getContext()).clear(imageView);
      imageView.setImageResource(defaultImageId);
    }
  }

  /**
   * Load image into imageView by URL and nothing if error occurred
   *
   * @param imageView where photo should be displayed
   * @param photoUrl  photo url
   */
  public static void setImageByUrl(ImageView imageView, @Nullable String photoUrl) {
    if (photoUrl != null && !photoUrl.trim().isEmpty()) {
      Glide.with(imageView.getContext())
          .load(photoUrl)
          .into(imageView);
    } else {
      Glide.with(imageView.getContext()).clear(imageView);
    }
  }

  /**
   * Return trip duration in "start - end" format.
   *
   * @return trip duration in dd MM yyyy format
   */
  public static String getDuration(LocalDate start, LocalDate end, String format) {
    return getDateFormatted(start, format) + " - " + getDateFormatted(end, format);
  }


  /**
   * Return LocalDate in provided format.
   *
   * @param date local date to format
   * @return date in dd MM yyyy format
   */
  public static String getDateFormatted(LocalDate date, String format) {
    return date.format(DateTimeFormatter.ofPattern(format));
  }
}
