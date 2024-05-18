package ru.hse.goodtrip.ui.profile.mytrips;

import static android.view.View.GONE;
import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.github.dhaval2404.imagepicker.ImagePicker;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.model.trips.City;
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

  FragmentPostEditorBinding binding;
  Trip trip;
  PostEditorViewModel postEditorViewModel;
  private final ActivityResultLauncher<Intent> pickMedia = registerForActivityResult(
      new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
          Intent data = result.getData();
          if (data != null && data.getData() != null) {
            postEditorViewModel.setPhoto(data.getData().toString());
          }
        }
      });

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
  }

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

    binding.postTitle.setFocusable(isEditModeOn);
    binding.postTitle.setFocusableInTouchMode(isEditModeOn);
    binding.postTitle.setClickable(isEditModeOn);
    binding.budgetLabel.setFocusable(isEditModeOn);
    binding.budgetLabel.setFocusableInTouchMode(isEditModeOn);
    binding.budgetLabel.setClickable(isEditModeOn);
  }

  private void showAddNewNoteDialog(AddNewNoteDialogFragment newNoteDialogFragment) {
    newNoteDialogFragment.show(getChildFragmentManager(), "dialog..."); // TODO
    getChildFragmentManager().executePendingTransactions();
    DisplayMetrics metrics = getResources().getDisplayMetrics();
    int width = metrics.widthPixels;
    newNoteDialogFragment.getDialog().getWindow()
        .setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);

    setupAddNoteDialog(newNoteDialogFragment);
  }

  private void setupAddNoteDialog(AddNewNoteDialogFragment newNoteDialogFragment) {
    final Button addNote = newNoteDialogFragment.binding.popupAddNote;
    final ImageButton closeButton = newNoteDialogFragment.binding.closeButton;
    addNote.setOnClickListener(v -> {
      String headline = newNoteDialogFragment.binding.noteHeadlineEditText.getText().toString();
      String text = newNoteDialogFragment.binding.noteTextEditText.getText().toString();
      String place = newNoteDialogFragment.binding.notePlaceEditText.getText().toString();

      // TODO
      postEditorViewModel.addNote(headline, text, place, postEditorViewModel.getPhoto());
      addNoteView(headline, text, postEditorViewModel.getPhoto(), place);
      newNoteDialogFragment.dismiss();
    });
    closeButton.setOnClickListener(v -> newNoteDialogFragment.dismiss());
  }

  private void showAddNewDestinationDialog(
      AddNewDestinationDialogFragment addNewDestinationDialogFragment) {
    addNewDestinationDialogFragment.show(getChildFragmentManager(), "dialog..."); // TODO
    getChildFragmentManager().executePendingTransactions();
    DisplayMetrics metrics = getResources().getDisplayMetrics();
    int width = metrics.widthPixels;
    addNewDestinationDialogFragment.getDialog().getWindow()
        .setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
    setupAddCountryDialog(addNewDestinationDialogFragment);
  }

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
      postEditorViewModel.addCountry(country, cities);
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


  private void loadRoute() {
    for (CountryVisit country : trip.getCountries()) {
      List<String> cities = new ArrayList<>();
      for (City city : country.getVisitedCities()) {
        cities.add(city.getName());
      }
      addCountryView(country.getCountry().getName(), cities);
    }
  }

  private void loadNotes() {
    for (Note note : trip.getNotes()) {
      addNoteView(note.getHeadline(), note.getNote(), note.getPhotoUrl(),
          note.getPlace().getName());
    }
  }

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

  private void uploadImageFromGallery() {
    ImagePicker.with(this)
        .cropSquare()
        .compress(180)
        .maxResultSize(180, 180)
        .createIntent(intent -> {
          pickMedia.launch(intent);
          return null;
        });
  }

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