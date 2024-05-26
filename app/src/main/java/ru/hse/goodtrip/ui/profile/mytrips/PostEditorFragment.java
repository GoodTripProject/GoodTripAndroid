package ru.hse.goodtrip.ui.profile.mytrips;

import static android.view.View.GONE;
import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.github.dhaval2404.imagepicker.ImagePicker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Setter;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.model.trips.City;
import ru.hse.goodtrip.data.model.trips.Coordinates;
import ru.hse.goodtrip.data.model.trips.Country;
import ru.hse.goodtrip.data.model.trips.CountryVisit;
import ru.hse.goodtrip.data.model.trips.Note;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.databinding.FragmentPostEditorBinding;
import ru.hse.goodtrip.databinding.ItemNoteBinding;
import ru.hse.goodtrip.ui.profile.mytrips.PostEditorDialogWindows.AddNewDestinationDialogFragment;
import ru.hse.goodtrip.ui.profile.mytrips.PostEditorDialogWindows.AddNewNoteDialogFragment;
import ru.hse.goodtrip.ui.trips.feed.FeedAdapter;

/**
 * A post editor fragment.
 */
public class PostEditorFragment extends Fragment {

  private static final String TAG = "PostEditor";
  FragmentPostEditorBinding binding;
  Trip trip;
  PostEditorViewModel postEditorViewModel;
  @Setter
  String currentNoteImageUrl;
  @Setter
  ImageView currentNoteImageView;
  /**
   * Starts intent that upload image from Camera or from Gallery. see
   * {@link #uploadPostImageFromGallery()}.
   */
  ActivityResultLauncher<Intent> pickPostImage = registerForActivityResult(
      new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
          Intent data = result.getData();
          if (data != null && data.getData() != null) {
            String newPhoto = data.getData().toString();
            setImageByUrl(binding.postImageView, newPhoto);
            Log.d(TAG, newPhoto);
            trip.setMainPhotoUrl(newPhoto);
            uploadImageToFirebase(data.getData());
          }
        }
      });
  /**
   * Starts intent that upload image from Camera or from Gallery. see
   * {@link #uploadNoteImageFromGallery()}.
   */
  ActivityResultLauncher<Intent> pickNoteImage = registerForActivityResult(
      new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
          Intent data = result.getData();
          if (data != null && data.getData() != null) {
            String newPhoto = data.getData().toString();
            setCurrentNoteImageUrl(newPhoto);
            setImageByUrl(currentNoteImageView, newPhoto);
            currentNoteImageView.setVisibility(View.VISIBLE);
            Log.d(TAG, newPhoto);
          }
        }
      });

  public void uploadImageToFirebase(Uri localPhotoUrl) {
    try {
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(),
          localPhotoUrl);
      // do anything you want
    } catch (IOException e) {
      // TODO: ??
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar())
        .setDisplayHomeAsUpEnabled(true);
    Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar())
        .setDisplayShowHomeEnabled(true);
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.GONE);
  }

  @Override
  public void onStop() {
    super.onStop();
    Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar())
        .setDisplayHomeAsUpEnabled(true);
    Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar())
        .setDisplayShowHomeEnabled(true);
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.VISIBLE);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentPostEditorBinding.inflate(inflater, container, false);
    postEditorViewModel = new ViewModelProvider(this).get(PostEditorViewModel.class);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    Bundle args = getArguments();
    if (args != null) {
      this.trip = (Trip) args.get(FeedAdapter.POST_ARG);
      postEditorViewModel.setTrip(trip);
    }
    if (trip != null) {
      postEditorViewModel.setTrip(trip);
      setImageByUrl(binding.postImageView, trip.getMainPhotoUrl(), R.drawable.kazantip);
      binding.budgetLabel.setText(Integer.toString(trip.getMoneyInUsd()));
      binding.postTitle.setText(trip.getTitle());
      loadRoute();
      loadNotes();
      setUpButtonClickListeners();
    }
  }

  private void setUpButtonClickListeners() {
    AddNewDestinationDialogFragment addNewDestinationDialogFragment = new AddNewDestinationDialogFragment();
    binding.addNewCountry.setOnClickListener(
        v -> showAddNewDestinationDialog(addNewDestinationDialogFragment));

    AddNewNoteDialogFragment newNoteDialogFragment = new AddNewNoteDialogFragment();
    binding.addNewNote.setOnClickListener(v -> showAddNewNoteDialog(newNoteDialogFragment));
    binding.postButton.setOnClickListener(this::newPost);

    binding.editModeButton.setOnClickListener(v -> switchEditMode(true));
    binding.saveChangesButton.setOnClickListener(v -> saveChanges());
    binding.pickImageButton.setOnClickListener(v -> uploadPostImageFromGallery());
  }

  /**
   * Saves trip changes.
   */
  private void saveChanges() {
    new AlertDialog.Builder(getContext())
        .setTitle("Save changes")
        .setMessage("Are you sure?")
        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
          trip.setTitle(binding.postTitle.getText().toString());
          trip.setMoneyInUsd(Integer.parseInt(binding.budgetLabel.getText().toString()));

          postEditorViewModel.setTrip(trip);
          postEditorViewModel.saveTrip();
          switchEditMode(false);
        })
        .setNegativeButton(android.R.string.no, null).show();
  }

  /**
   * Switching UI to EditMode.
   *
   * @param isEditModeOn is edit mode on.
   */
  private void switchEditMode(boolean isEditModeOn) {
    int newVisibility = isEditModeOn ? View.VISIBLE : View.GONE;

    int currentVisibility = (newVisibility == View.GONE) ? View.VISIBLE : View.GONE;
    binding.editModeLayout.setVisibility(currentVisibility);
    binding.postButton.setVisibility(currentVisibility);

    binding.editBudgetButton.setVisibility(newVisibility);
    binding.notesEditButton.setVisibility(newVisibility);
    binding.routeEditButton.setVisibility(newVisibility);
    binding.editTitleButton.setVisibility(newVisibility);
    binding.saveChangesButton.setVisibility(newVisibility);
    binding.pickImageButton.setVisibility(newVisibility);

    binding.postTitle.setFocusable(isEditModeOn);
    binding.postTitle.setFocusableInTouchMode(isEditModeOn);
    binding.postTitle.setClickable(isEditModeOn);
    binding.budgetLabel.setFocusable(isEditModeOn);
    binding.budgetLabel.setFocusableInTouchMode(isEditModeOn);
    binding.budgetLabel.setClickable(isEditModeOn);
  }

  /**
   * Shows AddNewNoteDialog window.
   *
   * @param newNoteDialogFragment current dialog.
   */
  private void showAddNewNoteDialog(AddNewNoteDialogFragment newNoteDialogFragment) {
    newNoteDialogFragment.show(getChildFragmentManager(), "dialog..."); // TODO
    getChildFragmentManager().executePendingTransactions();
    DisplayMetrics metrics = getResources().getDisplayMetrics();
    int width = metrics.widthPixels;
    newNoteDialogFragment.getDialog().getWindow()
        .setLayout((8 * width) / 9, ViewGroup.LayoutParams.WRAP_CONTENT);

    setupAddNoteDialog(newNoteDialogFragment);
    newNoteDialogFragment.getDialog().getWindow()
        .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
  }

  /**
   * Setup AddNewNoteDialogFragment fields.
   *
   * @param newNoteDialogFragment current dialog.
   */
  private void setupAddNoteDialog(AddNewNoteDialogFragment newNoteDialogFragment) {
    final Button addNote = newNoteDialogFragment.binding.popupAddNote;
    final ImageButton closeButton = newNoteDialogFragment.binding.closeButton;
    final ImageView pickImageButton = newNoteDialogFragment.binding.noteImagePicker;
    final ImageView noteImageView = newNoteDialogFragment.binding.noteImageView;
    final TextView notePhotoUrl = newNoteDialogFragment.binding.photoUrl;
    pickImageButton.setOnClickListener(v -> {
      setCurrentNoteImageView(noteImageView);
      uploadNoteImageFromGallery();
      notePhotoUrl.setText(currentNoteImageUrl);
    });
    addNote.setOnClickListener(v -> {
      String headline = newNoteDialogFragment.binding.noteHeadlineEditText.getText().toString();
      String text = newNoteDialogFragment.binding.noteTextEditText.getText().toString();
      String place = newNoteDialogFragment.binding.notePlaceEditText.getText().toString();

      String newNotePhotoUrl = currentNoteImageUrl;
      addNote(headline, text, place, newNotePhotoUrl);
      addNoteView(headline, text, newNotePhotoUrl, place);
      newNoteDialogFragment.dismiss();
    });
    closeButton.setOnClickListener(v -> newNoteDialogFragment.dismiss());
  }

  public void addNote(String noteHeadline, String noteText, String place, String photo) {
    trip.getNotes().add(new Note(noteHeadline, noteText, photo,
        new City(place, new Coordinates(0, 0), new Country("", new Coordinates(0, 0)))));
  }

  /**
   * Setup AddNewDestinationDialogFragment fields and AddCountryDialog.
   *
   * @param addNewDestinationDialogFragment current dialog.
   */
  private void showAddNewDestinationDialog(
      AddNewDestinationDialogFragment addNewDestinationDialogFragment) {
    addNewDestinationDialogFragment.show(getChildFragmentManager(), "dialog..."); // TODO
    getChildFragmentManager().executePendingTransactions();
    DisplayMetrics metrics = getResources().getDisplayMetrics();
    int width = metrics.widthPixels;
    addNewDestinationDialogFragment.getDialog().getWindow()
        .setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
    setupAddCountryDialog(addNewDestinationDialogFragment);

    addNewDestinationDialogFragment.getDialog().getWindow()
        .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

  }

  /**
   * Setup AddCountryDialog fields.
   *
   * @param dialog current dialog.
   */
  private void setupAddCountryDialog(AddNewDestinationDialogFragment dialog) {
    final AutoCompleteTextView autoCompleteTextViewCountries = dialog.binding.enterCountryName;
    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
        android.R.layout.select_dialog_item,
        postEditorViewModel.getCountries().toArray(new String[0]));
    autoCompleteTextViewCountries.setThreshold(0);
    autoCompleteTextViewCountries.setAdapter(adapter);
    final Button addCountry = dialog.binding.popupAddCountry;
    final Button addCity = dialog.binding.popupAddCity;
    final ImageButton closeButton = dialog.binding.closeButton;
    LinearLayout citiesLayout = dialog.binding.addCities;
    addCountry.setOnClickListener(v -> {
      String country = autoCompleteTextViewCountries.getText().toString();
      List<String> cities = new ArrayList<>();
      for (int index = 0; index < citiesLayout.getChildCount(); ++index) {
        cities.add(
            ((TextView) (citiesLayout.getChildAt(index)).findViewById(
                R.id.enter_city_name)).getText().toString());
      }
      addCountry(country, cities);
      addCountryView(country, cities);
      dialog.dismiss();
    });

    addCity.setOnClickListener(v -> {
      View view = LayoutInflater.from(requireContext())
          .inflate(R.layout.item_add_city, null);
      view.setLayoutParams(
          new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
              LinearLayout.LayoutParams.WRAP_CONTENT));
      citiesLayout.addView(view);
      citiesLayout.refreshDrawableState();
    });

    closeButton.setOnClickListener(v -> dialog.dismiss());
  }

  /**
   * add country to trip.
   *
   * @param countryName country name.
   * @param citiesName  name of cities.
   */
  public void addCountry(String countryName, List<String> citiesName) {
    Country country = new Country(countryName, new Coordinates(0, 0));
    List<City> cities = new ArrayList<>();
    for (String cityName : citiesName) {
      cities.add(new City(cityName, new Coordinates(0, 0), country));
    }
    CountryVisit countryVisit = new CountryVisit(country, cities);
    trip.getCountries().add(countryVisit);
  }

  /**
   * Render route view.
   */
  private void loadRoute() {
    for (CountryVisit country : trip.getCountries()) {
      List<String> cities = new ArrayList<>();
      for (City city : country.getVisitedCities()) {
        cities.add(city.getName());
      }
      addCountryView(country.getCountry().getName(), cities);
    }
  }

  /**
   * Render notes view.
   */
  private void loadNotes() {
    for (Note note : trip.getNotes()) {
      addNoteView(note.getHeadline(), note.getNote(), note.getPhotoUrl(),
          note.getPlace().getName());
    }
  }

  /**
   * Publish trip.
   *
   * @param view current view
   */
  private void newPost(View view) {
    new AlertDialog.Builder(getContext())
        .setTitle("Post trip")
        .setMessage("Are you sure?")
        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
          postEditorViewModel.postTrip();
          ((MainActivity) requireActivity()).getNavigationGraph().navigateUp();
          ((MainActivity) requireActivity()).getNavigationGraph().navigateUp();
        })
        .setNegativeButton(android.R.string.no, null).show();
  }

  /**
   * Starts intent that upload image by Camera or opens Gallery. Gets URI of image, stores it in
   * {@link #trip}. Image is uploaded in postImageView by
   * {@link ru.hse.goodtrip.ui.trips.feed.utils.Utils#setImageByUrl(ImageView, String)}
   */
  private void uploadPostImageFromGallery() {
    ImagePicker.with(this)
        .cropSquare()
        .compress(180)
        .maxResultSize(180, 180)
        .createIntent(intent -> {
          pickPostImage.launch(intent);
          return null;
        });
  }

  /**
   * Starts intent that upload image by Camera or opens Gallery. Gets URI of image, stores it in
   * {@link #currentNoteImageUrl}. Image is uploaded in {@link #currentNoteImageView} by
   * {@link ru.hse.goodtrip.ui.trips.feed.utils.Utils#setImageByUrl(ImageView, String)}
   */
  private void uploadNoteImageFromGallery() {
    ImagePicker.with(this)
        .cropSquare()
        .compress(180)
        .maxResultSize(180, 180)
        .createIntent(intent -> {
          pickNoteImage.launch(intent);
          return null;
        });
  }

  /**
   * Add country view in route layout.
   *
   * @param country country
   * @param cities  cities in country
   */
  private void addCountryView(String country, List<String> cities) {
    View countryView = LayoutInflater.from(requireContext())
        .inflate(R.layout.item_country, null);
    TextView countryTitle = countryView.findViewById(R.id.titleCountry);
    countryTitle.setText(country);
    if (country.trim().isEmpty()) {
      return;
    }
    LinearLayout citiesLayout = countryView.findViewById(R.id.cities);
    for (String city : cities) {
      if (city.trim().isEmpty()) {
        continue;
      }
      View cityView = LayoutInflater.from(requireContext())
          .inflate(R.layout.item_city, null);
      ((TextView) cityView.findViewById(R.id.cityelement)).setText(city);
      citiesLayout.addView(cityView);
    }
    binding.route.addView(countryView);
  }

  /**
   * Add note into notes layout.
   *
   * @param noteHeadline note headline
   * @param noteText     note as is
   * @param photo        photo url
   * @param place        place name
   */
  private void addNoteView(String noteHeadline, String noteText, String photo, String place) {
    ItemNoteBinding noteBinding = ItemNoteBinding.inflate(getLayoutInflater());
    noteBinding.noteHeadline.setText(noteHeadline);
    if (photo != null && !photo.trim().isEmpty()) {
      setImageByUrl(noteBinding.noteImageView, photo);
    } else {
      noteBinding.imageContainer.setVisibility(GONE);
    }
    noteBinding.noteText.setText(noteText);
    noteBinding.placeName.setText(place);
    binding.notes.addView(noteBinding.getRoot());
  }
}