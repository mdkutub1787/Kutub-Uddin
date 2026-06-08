import 'dart:io';
import 'package:country_picker/country_picker.dart';
import 'package:file_picker/file_picker.dart';
import 'package:fflipy/core/localization/app_localizations.dart';
import 'package:fflipy/core/errors/error_handler.dart';
import 'package:fflipy/core/utils/custom_text_field.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:dio/dio.dart';
import '../../core/routing/app_router.dart';
import '../../core/utils/dialog_helper.dart';
import '../../core/widgets/brand_app_bar.dart';
import '../../core/widgets/preloader.dart';
import '../../models/profile/profile_update_model.dart';
import '../../models/profile/user_profile_model.dart';
import '../../providers/profile_providers.dart';
import '../../core/theme/primary_button.dart';
import 'package:path/path.dart' as path;
import 'dart:ui';
import '../../core/theme/app_theme.dart';

final Dio dio = Dio(
  BaseOptions(
    receiveTimeout: const Duration(seconds: 60),
    connectTimeout: const Duration(seconds: 15),
    sendTimeout: const Duration(seconds: 15),
  ),
);

class UpdateProfileScreen extends ConsumerStatefulWidget {
  final UserProfileModel userProfile;

  const UpdateProfileScreen({super.key, required this.userProfile});

  @override
  ConsumerState<UpdateProfileScreen> createState() => _UpdateProfileScreenState();
}

class _UpdateProfileScreenState extends ConsumerState<UpdateProfileScreen> {
  final _formKey = GlobalKey<FormState>();

  late final Map<String, TextEditingController> _controllers;

  File? _profileImage;
  File? _documentFile;

  String? _selectedGender;
  String? _selectedRemitterType;
  String? _selectedDocumentType;
  String? _selectedCountry;
  String? _selectedIssueCountry;
  String? _selectedLanguage;
  String? _selectedOccupation;

  final List<String> _occupations = [
    "Lawyer",
    "Adult Entertainment Club / Prostitution Activities",
    "Administrative and Auxiliary Services Activities",
    "Automotive and Mechanical Activities",
    "Casino, Gambling and Betting Activities",
    "Scrap and Recycling Activities",
    "Education and Teaching Activities",
    "Medical and Related Activities",
    "Security, Guarding and Custody Activities",
    "Healthcare Services Activities",
    "Sports Activities",
    "Vehicle Import Activities",
    "Financial and Insurance Activities",
    "Real Estate Activities",
    "Professional, Scientific and Technical Activities",
    "Actor / Actress",
    "Administrative Staff",
    "Travel Agent",
    "Agriculture",
    "Bricklayer / Mason",
    "Architect",
    "Artisan / Craftsman",
    "Social Worker",
    "Athlete / Sportsperson",
    "Self-Employed",
    "Administrative Assistant",
    "Nursing and Geriatric Assistant",
    "Flight Attendant",
    "Kitchen Assistant",
    "Street Cleaner",
    "Cashier",
    "Waiter / Waitress",
    "Truck Driver",
    "Butcher",
    "Carpenter",
    "Forklift Operator",
    "Artistic, Recreational and Entertainment Clubs",
    "Cook / Chef",
    "Lottery and Gambling Sales",
    "Wholesale Trader",
    "Retail Trader",
    "Art, Antiques, Philatelic and Numismatic Trade",
    "Jewelry, Precious Stones and Metals Trade",
    "Machinery Operator",
    "Janitor / Caretaker",
    "Doorman / Porter",
    "Construction",
    "Constructor / Builder",
    "Tailor / Seamstress",
    "Glazier",
    "Child Caregiver",
    "Shop Assistant",
    "Unemployed",
    "Executives / Shareholders of Electronic Money Companies",
    "Executives / Shareholders of Virtual Currency Companies",
    "Executives / Shareholders of Exchange Offices",
    "Doctor / Dentist / Surgeon / Specialist",
    "Electrician",
    "Domestic Worker",
    "Public Sector Employee",
    "Private Sector Employee",
    "Private Employee (Salaried)",
    "Electronic Money Company Employee",
    "Virtual Currency Company Employee",
    "Nurse",
    "Beautician / Esthetician",
    "Vocational Student",
    "University Student",
    "Pharmacist",
    "Plumber",
    "Photographer",
    "Livestock Farmer",
    "Geriatric Care",
    "Labor Manager",
    "Security Guard",
    "Tourist Guide",
    "Hospitality",
    "Import of Telephony and Electronic Products",
    "Manufacturing Industry",
    "IT Specialist",
    "Civil Engineer",
    "Minimum Living Income Recipient",
    "Gardener",
    "Retired",
    "Vehicle Cleaner",
    "Broadcaster / Announcer",
    "Mechanic",
    "Medicine",
    "Courier / Messenger",
    "Military",
    "Warehouse Worker",
    "Baggage Handler",
    "Nanny / Childcare",
    "Occupation",
    "Machine Operator",
    "Other Activities",
    "Baker",
    "Unemployed with Benefits",
    "Unemployed without Benefits",
    "Hairdresser / Stylist / Cosmetician",
    "Agricultural Laborer",
    "Politically Exposed Person (PEP)",
    "Fishmonger",
    "Painter",
    "Police Officer",
    "Doorman",
    "Arms and Military Production and Distribution",
    "Professions",
    "Teacher",
    "Receptionist",
    "Watchmaker",
    "Delivery Person",
    "Stock Replenisher",
    "Forestry",
    "Cleaning Services",
    "Parcel Services",
    "Lifeguard",
    "Welder",
    "Unemployment Subsidy",
    "Upholsterer",
    "Taxi Driver / Chauffeur",
    "Call Center Operator",
    "Transport and Storage",
    "Street Vendor",
    "Veterinarian",
    "Shoemaker"
  ];

  bool _isLoading = false;
  bool _isSubmitting = false;

  final Set<String> _requiredFields = {
    'firstname',
    'lastname',
    'username',
    'email',
    'phone',
    'date_of_birth',
    'place_of_birth',
    'nationality',
    'genderType',
    'documentType',
    'documentUpload',
    'document_id_number',
    'issue_country_code',
    'document_issue_date',
    'document_expiry_date',
    'address',
    'country_id',
    'city',
    'state',
    'post_code',
    'image',
  };

  @override
  void initState() {
    super.initState();
    _controllers = {
      'firstname': TextEditingController(text: widget.userProfile.firstname),
      'lastname': TextEditingController(text: widget.userProfile.lastname),
      'username': TextEditingController(text: widget.userProfile.username),
      'email': TextEditingController(text: widget.userProfile.email),
      'phone': TextEditingController(text: widget.userProfile.phone),
      'date_of_birth': TextEditingController(text: widget.userProfile.dateOfBirth),
      'place_of_birth': TextEditingController(text: widget.userProfile.placeOfBirth),
      'occupation': TextEditingController(text: widget.userProfile.occupation),
      'address': TextEditingController(text: widget.userProfile.address),
      'post_code': TextEditingController(text: widget.userProfile.postCode),
      'city': TextEditingController(text: widget.userProfile.city),
      'state': TextEditingController(text: widget.userProfile.state),
      'nationality': TextEditingController(text: widget.userProfile.nationality),
      'source_of_fund': TextEditingController(text: widget.userProfile.sourceOfFund),
      'declaration_amount': TextEditingController(text: widget.userProfile.declarationAmount),
      'declaration_start_date': TextEditingController(text: widget.userProfile.declarationStartDate),
      'declaration_end_date': TextEditingController(text: widget.userProfile.declarationEndDate),
      'monthly_income': TextEditingController(text: widget.userProfile.yearlyIncome),
      'daily_limit': TextEditingController(text: widget.userProfile.dailyLimit),
      'monthly_limit': TextEditingController(text: widget.userProfile.monthlyLimit),
      'yearly_limit': TextEditingController(text: widget.userProfile.yearlyLimit),
      'remarks': TextEditingController(text: widget.userProfile.remarks),
      'document_id_number': TextEditingController(text: widget.userProfile.documentIdNumber),
      'document_issue_date': TextEditingController(text: widget.userProfile.documentIssueDate),
      'document_expiry_date': TextEditingController(text: widget.userProfile.documentExpiryDate),
    };

    _selectedGender = widget.userProfile.genderType;
    _selectedRemitterType = widget.userProfile.remitterType;
    _selectedDocumentType = widget.userProfile.documentType;
    _selectedCountry = widget.userProfile.countryId;
    _selectedIssueCountry = widget.userProfile.issueCountryCode;
    _selectedLanguage = widget.userProfile.languageId;
    _selectedOccupation = widget.userProfile.occupation;

    // Check if initial occupation is in the list, if not but exists, we might want to add it or just set it
    if (_selectedOccupation != null && _selectedOccupation!.isNotEmpty && !_occupations.contains(_selectedOccupation)) {
      // Logic to handle custom occupation if needed, or just let it display if dropdown supports custom values
      // Assuming dropdown maps to what's available. If the backend has value not in list, it might be an issue.
      // We will leave it as is, or add it to list temporarily?
      // For now let's just use it as initialized.
    }
  }

  @override
  void dispose() {
    _controllers.forEach((_, controller) => controller.dispose());
    super.dispose();
  }

  Future<void> _pickProfileImage() async {
    final pickedFile = await ImagePicker().pickImage(
      source: ImageSource.gallery,
      imageQuality: 50,
      maxWidth: 800,
      maxHeight: 800,
    );
    if (pickedFile != null) {
      setState(() {
        _profileImage = File(pickedFile.path);
      });
    }
  }

  Future<void> _pickDocumentFile() async {
    final result = await FilePicker.platform.pickFiles();
    if (result != null && result.files.single.path != null) {
      setState(() {
        _documentFile = File(result.files.single.path!);
      });
    }
  }

  Future<void> _selectDate(BuildContext context, TextEditingController controller) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1900),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      setState(() {
        controller.text = DateFormat('yyyy-MM-dd').format(picked);
      });
    }
  }

  Future<void> _onUpdateProfile() async {
    if (_isSubmitting) return;

    if (!_formKey.currentState!.validate()) {
      DialogHelper.showSnackBar(context, context.tr('Please fill all the required fields'), isError: true);
      return;
    }

    final monthlyIncome = double.tryParse(_controllers['monthly_income']!.text) ?? 0;
    if (monthlyIncome <= 0) {
      DialogHelper.showSnackBar(context, context.tr('Monthly income must be greater than zero'), isError: true);
      return;
    }

    final declarationAmount = double.tryParse(_controllers['declaration_amount']!.text) ?? 0;
    if (declarationAmount > (monthlyIncome * 12)) {
       final proceed = await DialogHelper.showConfirmationDialog(
         context: context,
         title: context.tr('Warning'),
         message: context.tr('Declaration amount seems unusually high compared to income. Do you want to proceed?'),
       );
       if (proceed != true) return;
    }

    final dailyLimit = double.tryParse(_controllers['daily_limit']!.text) ?? 0;
    final monthlyLimit = double.tryParse(_controllers['monthly_limit']!.text) ?? 0;
    final yearlyLimit = double.tryParse(_controllers['yearly_limit']!.text) ?? 0;

    if (dailyLimit > monthlyLimit && monthlyLimit > 0) {
      DialogHelper.showSnackBar(context, context.tr('Daily limit cannot exceed monthly limit'), isError: true);
      return;
    }
    if (monthlyLimit > yearlyLimit && yearlyLimit > 0) {
      DialogHelper.showSnackBar(context, context.tr('Monthly limit cannot exceed yearly limit'), isError: true);
      return;
    }

    if (yearlyLimit > (monthlyIncome * 12) && monthlyIncome > 0) {
      final proceed = await DialogHelper.showConfirmationDialog(
        context: context,
        title: context.tr('Warning'),
        message: context.tr('Yearly limit exceeds your total annual income. Do you want to proceed?'),
      );
      if (proceed != true) return;
    }

    if (_profileImage == null && (widget.userProfile.image == null || widget.userProfile.image!.isEmpty)) {
      DialogHelper.showSnackBar(context, context.tr('Please select a profile image'), isError: true);
      return;
    }

    if (_documentFile == null && widget.userProfile.documentUpload == null) {
      DialogHelper.showSnackBar(context, context.tr('Please choose a document'), isError: true);
      return;
    }

    setState(() {
      _isSubmitting = true;
      _isLoading = true;
    });

    final countriesData = ref.read(activeCountriesProvider);
    String? issueCountryIsoCode;
    if (_selectedIssueCountry != null && countriesData.hasValue) {
      final countries = countriesData.value!;
      try {
        final selectedCountry = countries.firstWhere((c) => c.id.toString() == _selectedIssueCountry || c.isoCode == _selectedIssueCountry);
        issueCountryIsoCode = selectedCountry.isoCode;
      } catch (e) {
        debugPrint("Could not find country to get iso_code for '$_selectedIssueCountry'");
        if (_selectedIssueCountry != null && _selectedIssueCountry!.length < 4) {
          issueCountryIsoCode = _selectedIssueCountry;
        }
      }
    }

    final profileUpdateModel = ProfileUpdateModel(
      firstname: _controllers['firstname']!.text,
      lastname: _controllers['lastname']!.text,
      username: _controllers['username']!.text,
      dateOfBirth: _formatDateForApi(_controllers['date_of_birth']!.text),
      placeOfBirth: _controllers['place_of_birth']!.text,
      occupation: _selectedOccupation,
      languageId: _selectedLanguage,
      address: _controllers['address']!.text,
      postCode: _controllers['post_code']!.text,
      city: _controllers['city']!.text,
      state: _controllers['state']!.text,
      nationality: _controllers['nationality']!.text,
      sourceOfFund: _controllers['source_of_fund']!.text,
      declarationAmount: _controllers['declaration_amount']!.text.isNotEmpty ? _controllers['declaration_amount']!.text : '0',
      declarationStartDate: _formatDateForApi(_controllers['declaration_start_date']!.text),
      declarationEndDate: _formatDateForApi(_controllers['declaration_end_date']!.text),
      yearlyIncome: _controllers['monthly_income']!.text.isNotEmpty ? _controllers['monthly_income']!.text : '0',
      dailyLimit: _controllers['daily_limit']!.text.isNotEmpty ? _controllers['daily_limit']!.text : '0',
      monthlyLimit: _controllers['monthly_limit']!.text.isNotEmpty ? _controllers['monthly_limit']!.text : '0',
      yearlyLimit: _controllers['yearly_limit']!.text.isNotEmpty ? _controllers['yearly_limit']!.text : '0',
      remarks: _controllers['remarks']!.text,
      documentIdNumber: _controllers['document_id_number']!.text,
      documentIssueDate: _formatDateForApi(_controllers['document_issue_date']!.text),
      documentExpiryDate: _formatDateForApi(_controllers['document_expiry_date']!.text),
      genderType: _selectedGender,
      remitterType: _selectedRemitterType,
      documentType: _selectedDocumentType,
      countryId: _selectedCountry,
      issue_country_code: issueCountryIsoCode,
      image: _profileImage?.path,
      documentUpload: _documentFile?.path,
    );

    try {
      final message = await ref.read(profileViewModelProvider.notifier).updateUserProfile(profileUpdateModel);
      if (mounted) {
        DialogHelper.showSnackBar(context, context.tr(message), isError: false);
        if (Navigator.of(context).canPop()) {
          Navigator.of(context).pop(true);
        } else {
          context.go(AppRouter.profile);
        }
      }
    } catch (e) {
      if (mounted) {
        DialogHelper.showSnackBar(context, context.tr(ErrorHandler.getErrorMessage(e)), isError: true);
      }
    } finally {
      if (mounted) {
        setState(() {
          _isLoading = false;
          _isSubmitting = false;
        });
      }
    }
  }

  String? _formatDateForApi(String? date) {
    if (date == null || date.isEmpty) return date;
    try {
      // Try to parse parsing full date time string first
      final dateTime = DateTime.parse(date);
      return DateFormat('yyyy-MM-dd').format(dateTime);
    } catch (e) {
      // Return original if parsing fails (might already be formatted or invalid)
      return date;
    }
  }

  void _showFullImage(BuildContext context, String imageUrl) {
    showGeneralDialog(
      context: context,
      barrierDismissible: true,
      barrierLabel: '',
      barrierColor: Colors.black.withOpacity(0.9),
      pageBuilder: (context, anim1, anim2) => Scaffold(
        backgroundColor: Colors.transparent,
        body: Stack(
          alignment: Alignment.center,
          children: [
            InteractiveViewer(
              minScale: 0.5,
              maxScale: 4.0,
              child: Center(
                child: Image.network(
                  imageUrl,
                  fit: BoxFit.contain,
                  loadingBuilder: (context, child, loadingProgress) {
                    if (loadingProgress == null) return child;
                    return const Center(child: CircularProgressIndicator(color: Colors.white));
                  },
                  errorBuilder: (context, error, stackTrace) => const Icon(Icons.error, color: Colors.white),
                ),
              ),
            ),
            Positioned(
              top: MediaQuery.of(context).padding.top + 10,
              right: 10,
              child: IconButton(
                icon: const Icon(Icons.close, color: Colors.white, size: 30),
                onPressed: () => Navigator.pop(context),
              ),
            ),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    final userProfile = widget.userProfile;
    final isProfileIncomplete = userProfile.firstname == null ||
        userProfile.lastname == null ||
        userProfile.username == null ||
        userProfile.email == null ||
        userProfile.phone == null ||
        userProfile.dateOfBirth == null ||
        userProfile.placeOfBirth == null ||
        userProfile.nationality == null ||
        userProfile.genderType == null ||
        userProfile.documentType == null ||
        userProfile.documentUpload == null ||
        userProfile.documentIdNumber == null ||
        userProfile.issueCountryCode == null ||
        userProfile.documentIssueDate == null ||
        userProfile.documentExpiryDate == null ||
        userProfile.address == null ||
        userProfile.countryId == null ||
        userProfile.city == null ||
        userProfile.state == null ||
        userProfile.postCode == null ||
        userProfile.image == null;

    return Scaffold(
      appBar: BrandAppBar(
        automaticallyImplyLeading: !isProfileIncomplete,
        leading: isProfileIncomplete
            ? null
            : IconButton(
                icon: const Icon(Icons.arrow_back),
                onPressed: () => context.go(AppRouter.profile),
              ),
        title: Text(context.tr('Update Profile')),
        actions: isProfileIncomplete
            ? []
            : [
                IconButton(
                  icon: const Icon(Icons.home_outlined),
                  onPressed: () => context.go(AppRouter.home),
                ),
              ],
      ),
      body: GestureDetector(
        behavior: HitTestBehavior.translucent,
        onTap: () => FocusScope.of(context).unfocus(),
        child: SafeArea(
          child: _isLoading
              ? const Center(child: Preloader())
              : Form(
                  key: _formKey,
                  child: SingleChildScrollView(
                    padding: const EdgeInsets.all(16.0),
                    child: AutofillGroup(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          if (isProfileIncomplete)
                            Container(
                              margin: const EdgeInsets.only(bottom: 24),
                              padding: const EdgeInsets.all(16),
                              decoration: BoxDecoration(
                                color: theme.colorScheme.primaryContainer,
                                borderRadius: BorderRadius.circular(12),
                                border: Border.all(color: theme.colorScheme.primary),
                              ),
                              child: Row(
                                children: [
                                  Icon(Icons.info_outline, color: theme.colorScheme.onPrimaryContainer),
                                  const SizedBox(width: 12),
                                  Expanded(
                                    child: Text(
                                      context.tr('Please Complete Your Profile to enjoy all features of Fflipy.'),
                                      style: theme.textTheme.bodyMedium?.copyWith(
                                        color: theme.colorScheme.onPrimaryContainer,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            ),
                            _buildSectionHeader(context.tr('Profile Picture')),
                            const SizedBox(height: 12),
                            Center(child: _buildImagePicker()),
                           const SizedBox(height: 24),
                          _buildSectionHeader(context.tr('Personal Information')),
                          _buildTextField('firstname', context.tr('First Name'), autofillHints: [AutofillHints.givenName]),
                          _buildTextField('lastname', context.tr('Last Name'), autofillHints: [AutofillHints.familyName]),
                          _buildTextField('username', context.tr('Username'), enabled: false, autofillHints: [AutofillHints.username]),
                          _buildTextField('email', context.tr('Email Address'), enabled: false, keyboardType: TextInputType.emailAddress, autofillHints: [AutofillHints.email]),
                          _buildTextField('phone', context.tr('Phone Number'), enabled: false, keyboardType: TextInputType.phone, autofillHints: [AutofillHints.telephoneNumber]),
                          _buildDatePicker('date_of_birth', context.tr('Date of Birth')),
                          _buildTextField('place_of_birth', context.tr('Place of Birth')),
                          _buildGenderDropdown(),
                          _buildNationalityPicker(),
                          const SizedBox(height: 24),
                          _buildSectionHeader(context.tr('Address Information')),
                          _buildTextField('address', context.tr('Address'), autofillHints: [AutofillHints.fullStreetAddress]),
                          _buildTextField('city', context.tr('City'), autofillHints: [AutofillHints.addressCity]),
                          _buildTextField('state', context.tr('State/Division'), autofillHints: [AutofillHints.addressState]),
                          _buildTextField('post_code', context.tr('Post Code'), autofillHints: [AutofillHints.postalCode]),
                          _buildCountryDropdown('country_id', context.tr('Country'), _selectedCountry, (val) => setState(() => _selectedCountry = val)),
                          const SizedBox(height: 24),
                          _buildSectionHeader(context.tr('Financial Information')),
                          _buildOccupationDropdown(),
                          _buildRemitterTypeDropdown(),
                          _buildTextField('source_of_fund', context.tr('Source of Fund')),
                          _buildTextField('monthly_income', context.tr('Monthly Income'), keyboardType: TextInputType.number),
                          _buildTextField('daily_limit', context.tr('Daily Limit'), keyboardType: TextInputType.number, enabled: true),
                          _buildTextField('monthly_limit', context.tr('Monthly Limit'), keyboardType: TextInputType.number, enabled: true),
                          _buildTextField('yearly_limit', context.tr('Yearly Limit'), keyboardType: TextInputType.number, enabled: true),
                          _buildTextField('declaration_amount', context.tr('Declaration Amount'), keyboardType: TextInputType.number),
                          _buildDatePicker('declaration_start_date', context.tr('Declaration Start Date')),
                          _buildDatePicker('declaration_end_date', context.tr('Declaration End Date')),
                          const SizedBox(height: 24),
                          _buildSectionHeader(context.tr('Document Information')),
                          _buildDocumentTypeDropdown(),
                          _buildTextField('document_id_number', context.tr('Document ID Number')),
                          _buildIssueCountryPicker(),
                          _buildDatePicker('document_issue_date', context.tr('Document Issue Date')),
                          _buildDatePicker('document_expiry_date', context.tr('Document Expiry Date')),
                          _buildDocumentPicker(),
                          const SizedBox(height: 24),
                          _buildSectionHeader(context.tr('Preferences')),
                          _buildLanguageDropdown(),
                          _buildTextField('remarks', context.tr('Remarks')),
                          const SizedBox(height: 32),
                          if (_profileImage == null && (widget.userProfile.image == null || widget.userProfile.image!.isEmpty))
                            Container(
                              width: double.infinity,
                              padding: const EdgeInsets.all(16),
                              decoration: BoxDecoration(
                                color: theme.colorScheme.error.withAlpha(25),
                                borderRadius: BorderRadius.circular(12),
                                border: Border.all(color: theme.colorScheme.error.withAlpha(128)),
                              ),
                              child: Column(
                                children: [
                                  Icon(Icons.add_a_photo_outlined, size: 40, color: theme.colorScheme.error),
                                  const SizedBox(height: 12),
                                  Text(
                                    context.tr('Image Required'),
                                    style: theme.textTheme.titleMedium?.copyWith(
                                      color: theme.colorScheme.error,
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                  const SizedBox(height: 4),
                                  Text(
                                    context.tr('Please upload a profile photo to update your profile.'),
                                    textAlign: TextAlign.center,
                                    style: theme.textTheme.bodyMedium?.copyWith(
                                      color: theme.colorScheme.onSurface.withAlpha(179),
                                    ),
                                  ),
                                ],
                              ),
                            )
                          else
                            SizedBox(
                              width: double.infinity,
                              child: PrimaryButton(
                                onPressed: _isSubmitting ? null : _onUpdateProfile,
                                text: context.tr('Update Profile'),
                                isLoading: _isSubmitting,
                              ),
                            ),
                          const SizedBox(height: 200),
                        ],
                      ),
                    ),
                  ),
                ),
              ),
      ),
    );
  }

  Widget _buildSectionHeader(String title) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 16.0, top: 8.0),
      child: Text(title, style: Theme.of(context).textTheme.headlineSmall?.copyWith(color: Theme.of(context).colorScheme.primary)),
    );
  }

  Widget _buildTextField(String key, String label, {TextInputType? keyboardType, bool enabled = true, List<String>? autofillHints}) {
    final controller = _controllers[key];
    final isRequired = _requiredFields.contains(key);
    bool showAsterisk = isRequired && (controller?.text.isEmpty ?? true);
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: CustomTextField(
        controller: controller!,
        labelText: label,
        showAsterisk: showAsterisk,
        keyboardType: keyboardType ?? TextInputType.text,
        enabled: enabled,
        onChanged: (_) => setState(() {}),
        autofillHints: autofillHints,
        validator: (value) {
          if (isRequired && (value == null || value.isEmpty)) {
            return context.tr('This field is required');
          }

          if (keyboardType == TextInputType.number && value != null && value.isNotEmpty) {
            final double? val = double.tryParse(value);
            if (val == null) return context.tr('Invalid number');

            if (key == 'monthly_income' && val <= 0) {
              return context.tr('Monthly income must be greater than zero');
            }

            final incomeStr = _controllers['monthly_income']?.text;
            final double income = double.tryParse(incomeStr ?? '0') ?? 0;

            if (key == 'declaration_amount' && income > 0 && val > (income * 12)) {
               return context.tr('Declaration amount seems unusually high');
            }
          }

          return null;
        },
      ),
    );
  }

  Widget _buildDatePicker(String key, String label) {
    final controller = _controllers[key];
    final isRequired = _requiredFields.contains(key);
    bool showAsterisk = isRequired && (controller?.text.isEmpty ?? true);
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: GestureDetector(
        onTap: () async {
            await _selectDate(context, controller);
            setState(() {});
          },
        child: AbsorbPointer(
          child: CustomTextField(
            controller: controller!,
            labelText: label,
            showAsterisk: showAsterisk,
            suffixIcon: const Icon(Icons.calendar_today),
            validator: (value) {
              if (isRequired && (value == null || value.isEmpty)) {
                return context.tr('This field is required');
              }
              if (key == 'date_of_birth' && isRequired) {
                try {
                  final dob = DateTime.parse(value!);
                  final now = DateTime.now();
                  final age = now.year - dob.year - ((now.month < dob.month || (now.month == dob.month && now.day < dob.day)) ? 1 : 0);
                  if (age < 18) {
                    return context.tr('You must be at least 18 years old');
                  }
                } catch (e) {
                  return context.tr('Invalid date');
                }
              }
              if (key == 'document_expiry_date' && isRequired) {
                final issueDateStr = _controllers['document_issue_date']?.text;
                if (issueDateStr != null && issueDateStr.isNotEmpty) {
                  try {
                    final expiry = DateTime.parse(value!);
                    final issue = DateTime.parse(issueDateStr);
                    if (!expiry.isAfter(issue)) {
                      return context.tr('Expiry date must be after issue date');
                    }
                  } catch (e) {
                    return context.tr('Invalid date');
                  }
                }
              }
              return null;
            },
          ),
        ),
      ),
    );
  }

  Widget _buildImagePicker() {
    final theme = Theme.of(context);
    return Center(
      child: Stack(
        children: [
          CircleAvatar(
            radius: 50,
            backgroundColor: theme.colorScheme.surfaceContainerHighest,
            backgroundImage: _profileImage != null
                ? FileImage(_profileImage!)
                : (widget.userProfile.image != null && widget.userProfile.image!.isNotEmpty
                    ? NetworkImage(widget.userProfile.image!) as ImageProvider
                    : null),
            child: _profileImage == null && (widget.userProfile.image == null || widget.userProfile.image!.isEmpty)
                ? Icon(Icons.person, size: 50, color: theme.colorScheme.onSurfaceVariant)
                : null,
          ),
          Positioned(
            bottom: 0,
            right: 0,
            child: IconButton(
              icon: const Icon(Icons.camera_alt),
              onPressed: _pickProfileImage,
              style: IconButton.styleFrom(
                backgroundColor: theme.colorScheme.primary,
                foregroundColor: theme.colorScheme.onPrimary
              )
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildDocumentPicker() {
    final theme = Theme.of(context);
    Widget documentPreview;
    String? documentName;
    const String baseAssetUrl = "https://dev.fflipy.com/assets/uploads/users/";

    if (_documentFile != null) {
      final fileExtension = path.extension(_documentFile!.path).toLowerCase();
      if (['.jpg', '.jpeg', '.png'].contains(fileExtension)) {
        documentPreview = Image.file(
          _documentFile!,
          height: 100,
          width: 100,
          fit: BoxFit.cover,
          errorBuilder: (context, error, stackTrace) => Container(
            height: 100,
            width: 100,
            color: Colors.grey.shade200,
            child: const Icon(Icons.broken_image, size: 40, color: Colors.grey),
          ),
        );
      } else {
        documentPreview = Icon(Icons.insert_drive_file, size: 50, color: theme.colorScheme.primary);
      }
      documentName = _documentFile!.path.split('/').last;
    } else if (widget.userProfile.documentUpload != null && widget.userProfile.documentUpload!.isNotEmpty) {
      final documentFileName = widget.userProfile.documentUpload!;
      final fullDocumentUrl = baseAssetUrl + documentFileName;
      final fileExtension = path.extension(documentFileName).toLowerCase();
      if (['.jpg', '.jpeg', '.png'].contains(fileExtension)) {
        documentPreview = Image.network(
          fullDocumentUrl,
          height: 100,
          width: 100,
          fit: BoxFit.cover,
          errorBuilder: (context, error, stackTrace) => Container(
            height: 100,
            width: 100,
            color: Colors.grey.shade200,
            child: const Icon(Icons.broken_image, size: 40, color: Colors.grey),
          ),
        );
      } else {
        documentPreview = Icon(Icons.insert_drive_file, size: 50, color: theme.colorScheme.primary);
      }
      documentName = documentFileName.split('/').last;
    } else {
      documentPreview = const SizedBox.shrink();
      documentName = context.tr('No file chosen');
    }

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              ElevatedButton.icon(
                onPressed: _pickDocumentFile,
                icon: const Icon(Icons.attach_file),
                label: Text(context.tr('Choose Document')),
                style: ElevatedButton.styleFrom(
                  backgroundColor: theme.colorScheme.secondary,
                  foregroundColor: theme.colorScheme.onSecondary,
                ),
              ),
              const SizedBox(width: 10),
              Expanded(
                child: Text(
                  documentName,
                  style: theme.textTheme.bodySmall,
                  overflow: TextOverflow.ellipsis,
                ),
              ),
            ],
          ),
          const SizedBox(height: 10),
          if (_documentFile != null || (widget.userProfile.documentUpload != null && widget.userProfile.documentUpload!.isNotEmpty))
            GestureDetector(
              onTap: () {
                if (_documentFile != null) {
                   // Local file preview if possible or just show it's selected
                } else {
                  final documentFileName = widget.userProfile.documentUpload!;
                  final fullDocumentUrl = baseAssetUrl + documentFileName;
                  _showFullImage(context, fullDocumentUrl);
                }
              },
              child: Container(
                padding: const EdgeInsets.all(8),
                decoration: BoxDecoration(
                  border: Border.all(color: theme.dividerColor),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: documentPreview
              ),
            ),
          if (_documentFile == null && (widget.userProfile.documentUpload == null || widget.userProfile.documentUpload!.isEmpty) && _requiredFields.contains('documentUpload'))
            Padding(
              padding: const EdgeInsets.only(top: 8.0),
              child: Text(
                context.tr('This field is required'),
                style: TextStyle(color: Theme.of(context).colorScheme.error, fontSize: 12),
              ),
            ),
        ],
      ),
    );
  }

  Widget _buildGenderDropdown() {
    final List<Map<String, String>> genders = [
      {'id': '1', 'name': 'Male'},
      {'id': '2', 'name': 'Female'},
      {'id': '3', 'name': 'Others'},
    ];
    final isRequired = _requiredFields.contains('genderType');
    bool showAsterisk = isRequired && (_selectedGender == null || _selectedGender!.isEmpty);
    return CustomPopupDropdown(
      value: _selectedGender,
      items: genders,
      label: context.tr('Gender'),
      showAsterisk: showAsterisk,
      onChanged: (value) {
        setState(() {
          _selectedGender = value;
        });
      },
      validator: (value) {
        if (isRequired && (value == null || value.isEmpty)) {
          return context.tr('This field is required');
        }
        return null;
      },
    );
  }

  Widget _buildRemitterTypeDropdown() {
    final remitterTypes = ref.watch(remitterTypesProvider);
    return remitterTypes.when(
      data: (remitters) {
        String? dropdownValue = _selectedRemitterType;
        final isValueAnId = remitters.any((r) => r.id.toString() == dropdownValue);

        if (!isValueAnId) {
          try {
            final remitter = remitters.firstWhere((r) => r.name == dropdownValue);
            dropdownValue = remitter.id.toString();
          } catch (e) {
            dropdownValue = null;
          }
        }

        if (dropdownValue != null && !remitters.any((r) => r.id.toString() == dropdownValue)) {
            dropdownValue = null;
        }

        final items = remitters.map((item) => {
          'id': item.id.toString(),
          'name': item.name,
        }).toList();

        return CustomPopupDropdown(
          value: dropdownValue,
          items: items,
          label: context.tr('Remitter Type'),
          onChanged: (value) {
            setState(() {
              _selectedRemitterType = value;
            });
          },
          validator: (value) {
            if (value == null) {
              return context.tr('This field is required');
            }
            return null;
          },
        );
      },
      loading: () => const Preloader(),
      error: (err, stack) => Text(context.tr(ErrorHandler.getErrorMessage(err))),
    );
  }

  Widget _buildDocumentTypeDropdown() {
    final documentTypes = ref.watch(documentTypesProvider);
    return documentTypes.when(
      data: (documents) {
        final items = documents.map((item) => {
          'id': item.id.toString(),
          'name': item.documentType,
        }).toList();
        return CustomPopupDropdown(
          value: _selectedDocumentType,
          items: items,
          label: context.tr('Document Type'),
          onChanged: (value) {
            setState(() {
              _selectedDocumentType = value;
            });
          },
          validator: (value) {
            if (value == null) {
              return context.tr('This field is required');
            }
            return null;
          },
        );
      },
      loading: () => const Preloader(),
      error: (err, stack) => Text(context.tr(ErrorHandler.getErrorMessage(err))),
    );
  }

  Widget _buildCountryDropdown(String key, String label, String? selectedValue, void Function(String?) onChanged) {
    final countries = ref.watch(activeCountriesProvider);

    return countries.when(
      data: (countryList) {
        String? dropdownValue = selectedValue;
        final isValueAnId = countryList.any((c) => c.id.toString() == dropdownValue);

        if (!isValueAnId) {
          try {
            final country = countryList.firstWhere((c) => c.name == dropdownValue || c.isoCode == dropdownValue);
            dropdownValue = country.id.toString();
          } catch (e) {
            dropdownValue = null;
          }
        }

        if (dropdownValue != null && !countryList.any((c) => c.id.toString() == dropdownValue)) {
          dropdownValue = null;
        }

        final items = countryList.map((item) => {
          'id': item.id.toString(),
          'name': item.name,
          'flag': item.flag,
        }).toList();

        return CustomPopupDropdown(
          value: dropdownValue,
          items: items,
          label: label,
          onChanged: (value) {
            onChanged(value);
            if (key == 'country_id') {
              setState(() => _selectedCountry = value);
            } else if (key == 'issue_country_code') {
              setState(() => _selectedIssueCountry = value);
            }
          },
          validator: (value) {
            if (value == null) {
              return context.tr('This field is required');
            }
            return null;
          },
        );
      },
      loading: () => const Preloader(),
      error: (err, stack) => Text(context.tr(ErrorHandler.getErrorMessage(err))),
    );
  }

  Widget _buildLanguageDropdown() {
    final profileData = ref.watch(profileViewModelProvider);

    return profileData.when(
      data: (data) {
        final items = data.languages.map((item) => {
          'id': item.id.toString(),
          'name': item.name,
        }).toList();
        return CustomPopupDropdown(
          value: _selectedLanguage,
          items: items,
          label: context.tr('Language'),
          onChanged: (value) {
            setState(() {
              _selectedLanguage = value;
            });
          },
          validator: (value) {
            if (value == null) {
              return context.tr('This field is required');
            }
            return null;
          },
        );
      },
      loading: () => const SizedBox.shrink(),
      error: (err, stack) => Text(context.tr(ErrorHandler.getErrorMessage(err))),
    );
  }

  Widget _buildOccupationDropdown() {
    final items = _occupations.map((item) => {
      'id': item,
      'name': context.tr(item),
    }).toList();

    return CustomPopupDropdown(
      value: _selectedOccupation,
      items: items,
      label: context.tr('Occupation'),
      onChanged: (value) {
        setState(() {
          _selectedOccupation = value;
        });
      },
      validator: (value) {
        // Occupation is not in _requiredFields set initially seen in the file (lines 56-78),
        // but checking if it's there or logic requires it.
        // Line 56 set does NOT include 'occupation'.
        // So no validation required unless we want to enforce it.
        // Existing code didn't force it in validator of _buildTextField if not in _requiredFields.
        return null;
      },
    );
  }

  Widget _buildNationalityPicker() {
    final controller = _controllers['nationality'];
    final isRequired = _requiredFields.contains('nationality');
    bool showAsterisk = isRequired && (controller?.text.isEmpty ?? true);

    String? flagEmoji;
    if (controller?.text.isNotEmpty ?? false) {
      try {
        final country = CountryService().getAll().firstWhere(
          (element) => element.name.toLowerCase() == controller!.text.toLowerCase() ||
                       element.displayName.toLowerCase() == controller.text.toLowerCase(),
        );
        flagEmoji = country.flagEmoji;
      } catch (_) {}
    }

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: GestureDetector(
        onTap: () {
          showCountryPicker(
            context: context,
            showPhoneCode: false,
            onSelect: (Country country) {
              setState(() {
                controller.text = country.name;
              });
            },
          );
        },
        child: AbsorbPointer(
          child: CustomTextField(
            controller: controller!,
            labelText: context.tr('Nationality'),
            showAsterisk: showAsterisk,
            prefixIcon: flagEmoji != null
                ? Padding(
                    padding: const EdgeInsets.all(12.0),
                    child: Text(flagEmoji, style: const TextStyle(fontSize: 24)),
                  )
                : const Icon(Icons.flag),
            validator: (value) {
              if (isRequired && (value == null || value.isEmpty)) {
                return context.tr('This field is required');
              }
              return null;
            },
          ),
        ),
      ),
    );
  }

  Widget _buildIssueCountryPicker() {
    final isRequired = _requiredFields.contains('issue_country_code');
    bool showAsterisk = isRequired && (_selectedIssueCountry == null || _selectedIssueCountry!.isEmpty);

    final displayText = _selectedIssueCountry ?? '';
    final countryNameController = TextEditingController(text: displayText);
    String? flag;

    // Try to find full name if we have a code
    if (_selectedIssueCountry != null && _selectedIssueCountry!.isNotEmpty) {
       try {
         final country = Country.tryParse(_selectedIssueCountry!);
         if (country != null) {
           countryNameController.text = country.name;
           flag = country.flagEmoji;
         } else {
            final countriesData = ref.read(activeCountriesProvider);
            if (countriesData.hasValue) {
               try {
                 final c = countriesData.value!.firstWhere(
                   (e) => e.id.toString() == _selectedIssueCountry || e.isoCode == _selectedIssueCountry
                 );
                 countryNameController.text = c.name;
               } catch (_) {}
            }
         }
       } catch (_) {}
    }

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: GestureDetector(
        onTap: () {
          showCountryPicker(
            context: context,
            showPhoneCode: false,
            onSelect: (Country country) {
              setState(() {
                _selectedIssueCountry = country.countryCode;
              });
            },
          );
        },
        child: AbsorbPointer(
          child: CustomTextField(
            controller: countryNameController,
            labelText: context.tr('Issue Country'),
            showAsterisk: showAsterisk,
            prefixIcon: flag != null
                ? Padding(
                    padding: const EdgeInsets.all(12.0),
                    child: Text(flag, style: const TextStyle(fontSize: 24)),
                  )
                : const Icon(Icons.flag),
            validator: (value) {
              if (isRequired && (_selectedIssueCountry == null || _selectedIssueCountry!.isEmpty)) {
                return context.tr('This field is required');
              }
              return null;
            },
          ),
        ),
      ),
    );
  }
}

class CustomPopupDropdown extends StatelessWidget {
  final String? value;
  final List<Map<String, String>> items;
  final String label;
  final bool showAsterisk;
  final void Function(String?) onChanged;
  final String? Function(String?)? validator;

  const CustomPopupDropdown({
    Key? key,
    required this.value,
    required this.items,
    required this.label,
    required this.onChanged,
    this.validator,
    this.showAsterisk = false,
  }) : super(key: key);

  void _showFullImage(BuildContext context, String imageUrl) {
    showGeneralDialog(
      context: context,
      barrierDismissible: true,
      barrierLabel: '',
      barrierColor: Colors.black.withOpacity(0.9),
      pageBuilder: (context, anim1, anim2) => Scaffold(
        backgroundColor: Colors.transparent,
        body: Stack(
          alignment: Alignment.center,
          children: [
            InteractiveViewer(
              minScale: 0.5,
              maxScale: 4.0,
              child: Center(
                child: Image.network(
                  imageUrl,
                  fit: BoxFit.contain,
                  loadingBuilder: (context, child, loadingProgress) {
                    if (loadingProgress == null) return child;
                    return const Center(child: CircularProgressIndicator(color: Colors.white));
                  },
                  errorBuilder: (context, error, stackTrace) => const Icon(Icons.error, color: Colors.white),
                ),
              ),
            ),
            Positioned(
              top: MediaQuery.of(context).padding.top + 10,
              right: 10,
              child: IconButton(
                icon: const Icon(Icons.close, color: Colors.white, size: 30),
                onPressed: () => Navigator.pop(context),
              ),
            ),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final colorScheme = theme.colorScheme;
    final selectedName = items.firstWhere((item) => item['id'] == value, orElse: () => {'name': ''})['name'] ?? '';
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 6.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Text(label, style: theme.textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold, fontSize: 14, color: colorScheme.onSurface)),
              if (showAsterisk)
                Text(' *', style: TextStyle(color: colorScheme.error, fontSize: 13)),
            ],
          ),
          const SizedBox(height: 4),
          GestureDetector(
            onTap: () async {
              final selected = await showGeneralDialog<String>(
                context: context,
                barrierDismissible: true,
                barrierLabel: '',
                barrierColor: colorScheme.onSurface.withAlpha((0.18 * 255).toInt()),
                transitionDuration: const Duration(milliseconds: 220),
                pageBuilder: (context, anim1, anim2) {
                  return const SizedBox.shrink();
                },
                transitionBuilder: (context, anim1, anim2, child) {
                  return _DropdownSearchDialog(
                    label: label,
                    items: items,
                    value: value,
                  );
                },
              );
              if (selected != null) {
                onChanged(selected);
              }
            },
            child: AnimatedContainer(
              duration: const Duration(milliseconds: 180),
              curve: Curves.easeInOut,
              padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
              decoration: BoxDecoration(
                color: colorScheme.surface,
                borderRadius: BorderRadius.circular(12),
                boxShadow: [
                  BoxShadow(
                    color: colorScheme.onSurface.withAlpha((0.08 * 255).toInt()),
                    blurRadius: 10,
                    offset: const Offset(0, 2),
                  ),
                ],
                border: Border.all(
                  color: (value?.isEmpty ?? true) ? theme.dividerColor : colorScheme.primary,
                  width: 1.2,
                ),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  AnimatedSwitcher(
                    duration: const Duration(milliseconds: 200),
                    child: Text(
                      selectedName.isNotEmpty ? selectedName : 'Select...',
                      key: ValueKey(selectedName),
                      style: TextStyle(
                        color: (value?.isEmpty ?? true) ? colorScheme.onSurfaceVariant : colorScheme.onSurface,
                        fontWeight: FontWeight.w600,
                        fontSize: 13,
                        letterSpacing: 0.1,
                      ),
                    ),
                  ),
                  Icon(Icons.expand_more, color: colorScheme.primary, size: 20),
                ],
              ),
            ),
          ),
          if (validator != null)
            Builder(
              builder: (context) {
                final error = validator!(value);
                if (error != null) {
                  return Padding(
                    padding: const EdgeInsets.only(top: 2.0, left: 2.0),
                    child: Text(error, style: TextStyle(color: colorScheme.error, fontSize: 11)),
                  );
                }
                return const SizedBox.shrink();
              },
            ),
        ],
      ),
    );
  }
}

class _DropdownSearchDialog extends StatefulWidget {
  final String label;
  final List<Map<String, String>> items;
  final String? value;
  const _DropdownSearchDialog({required this.label, required this.items, required this.value});
  @override
  State<_DropdownSearchDialog> createState() => _DropdownSearchDialogState();
}

class _DropdownSearchDialogState extends State<_DropdownSearchDialog> {
  String searchText = '';
  final FocusNode _searchFocus = FocusNode();
  @override
  void dispose() {
    _searchFocus.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final colorScheme = theme.colorScheme;
    List<Map<String, String>> filteredItems = widget.items;
    if (searchText.isNotEmpty) {
      filteredItems = widget.items.where((item) => (item['name'] ?? '').toLowerCase().contains(searchText.toLowerCase())).toList();
    }
    return Opacity(
      opacity: 1.0,
      child: Transform.scale(
        scale: 1.0,
        child: Center(
          child: Material(
            color: Colors.transparent,
            child: ClipRRect(
              borderRadius: BorderRadius.circular(20),
              child: BackdropFilter(
                filter: ImageFilter.blur(sigmaX: 18, sigmaY: 18),
                child: Container(
                  width: MediaQuery.of(context).size.width * 0.90,
                  constraints: const BoxConstraints(maxHeight: 340),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(20),
                    color: colorScheme.surface,
                    boxShadow: [
                      BoxShadow(
                        color: colorScheme.onSurface.withAlpha((0.10 * 255).toInt()),
                        blurRadius: 24,
                        offset: const Offset(0, 8),
                      ),
                    ],
                    border: Border.all(color: theme.dividerColor, width: 1.0),
                  ),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Container(
                        decoration: const BoxDecoration(
                          gradient: LinearGradient(
                            colors: [AppTheme.topBarGradientLeft, AppTheme.topBarGradientRight],
                            begin: Alignment.topLeft,
                            end: Alignment.bottomRight,
                          ),
                          borderRadius: BorderRadius.only(
                            topLeft: Radius.circular(20),
                            topRight: Radius.circular(20),
                          ),
                        ),
                        child: Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
                          child: Row(
                            children: [
                              Expanded(
                                child: Text(
                                  widget.label,
                                  style: theme.textTheme.titleMedium?.copyWith(
                                    fontWeight: FontWeight.bold,
                                    color: colorScheme.onSurface,
                                    fontSize: 14,
                                  ),
                                ),
                              ),
                              InkWell(
                                borderRadius: BorderRadius.circular(16),
                                onTap: () => Navigator.pop(context),
                                child: Padding(
                                  padding: const EdgeInsets.all(2.0),
                                  child: Icon(Icons.close_rounded, color: colorScheme.onSurfaceVariant, size: 22),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                        child: TextField(
                          focusNode: _searchFocus,
                          autofocus: false,
                          onTap: () => _searchFocus.requestFocus(),
                          onChanged: (val) => setState(() => searchText = val),
                          decoration: InputDecoration(
                            hintText: 'Search...',
                            prefixIcon: Icon(Icons.search, size: 18, color: colorScheme.primary),
                            isDense: true,
                            contentPadding: const EdgeInsets.symmetric(vertical: 6, horizontal: 8),
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(10),
                              borderSide: BorderSide(color: theme.dividerColor),
                            ),
                          ),
                          style: theme.textTheme.bodySmall?.copyWith(fontSize: 12, color: colorScheme.onSurface),
                        ),
                      ),
                      Divider(height: 1, thickness: 1, color: theme.dividerColor),
                      Flexible(
                        child: filteredItems.isEmpty
                            ? Padding(
                                padding: const EdgeInsets.all(16.0),
                                child: Text('No data found', style: theme.textTheme.bodyMedium?.copyWith(fontSize: 12, color: colorScheme.onSurfaceVariant)),
                              )
                            : ListView.separated(
                                padding: EdgeInsets.zero,
                                shrinkWrap: true,
                                itemCount: filteredItems.length,
                                separatorBuilder: (_, __) => Divider(height: 1, thickness: 0.5, color: theme.dividerColor),
                                itemBuilder: (context, idx) {
                                  final item = filteredItems[idx];
                                  final isSelected = item['id'] == widget.value;
                                  return InkWell(
                                    borderRadius: BorderRadius.circular(10),
                                    splashColor: colorScheme.primary.withAlpha((0.08 * 255).toInt()),
                                    highlightColor: colorScheme.primary.withAlpha((0.04 * 255).toInt()),
                                    onTap: () => Navigator.pop(context, item['id']),
                                    child: AnimatedContainer(
                                      duration: const Duration(milliseconds: 120),
                                      curve: Curves.easeInOut,
                                      decoration: BoxDecoration(
                                        color: isSelected ? colorScheme.primary.withAlpha((0.18 * 255).toInt()) : Colors.transparent,
                                        borderRadius: BorderRadius.circular(10),
                                      ),
                                      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                                      child: Row(
                                        children: [
                                          if (item['flag'] != null && item['flag']!.isNotEmpty)
                                            Padding(
                                              padding: const EdgeInsets.only(right: 6.0),
                                              child: Image.network(
                                                item['flag']!,
                                                width: 18,
                                                height: 14,
                                                fit: BoxFit.cover,
                                                errorBuilder: (context, error, stackTrace) => SizedBox(width: 18, height: 14, child: Icon(Icons.flag, color: colorScheme.onSurfaceVariant, size: 12)),
                                              ),
                                            ),
                                          Expanded(
                                            child: Text(
                                              item['name'] ?? '',
                                              style: theme.textTheme.bodySmall?.copyWith(
                                                fontSize: 12,
                                                fontWeight: isSelected ? FontWeight.bold : FontWeight.w500,
                                                color: isSelected ? colorScheme.onSurface : colorScheme.onSurfaceVariant,
                                                letterSpacing: 0.1,
                                              ),
                                            ),
                                          ),
                                          if (isSelected)
                                            Icon(Icons.check_circle_rounded, color: colorScheme.primary, size: 16),
                                        ],
                                      ),
                                    ),
                                  );
                                },
                              ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}





