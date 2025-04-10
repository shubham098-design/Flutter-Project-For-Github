import 'package:get/get.dart';
import 'package:supabase_flutter/supabase_flutter.dart';


class SupabaseServices extends GetxService{
  String SUPABASE_URL = "https://hzheqrvksvizrowvaxli.supabase.co";
  String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh6aGVxcnZrc3ZpenJvd3ZheGxpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDIwNDM4MzUsImV4cCI6MjA1NzYxOTgzNX0.rH1FK3SOnub96z9zz-20uhyGoljkCSh7RFDw9S-MuBU";


  Rx<User?> currentUser = Rx<User?>(null);
  static final SupabaseClient supabaseClient = Supabase.instance.client;

  @override
  void onInit() async{
    await Supabase.initialize(url: SUPABASE_URL, anonKey: SUPABASE_KEY);
    currentUser.value = supabaseClient.auth.currentUser;
    listenAuthChanges();
    super.onInit();
  }

  void listenAuthChanges() => supabaseClient.auth.onAuthStateChange.listen((data) {
    final AuthChangeEvent event = data.event;
    if(event == AuthChangeEvent.userUpdated){
      currentUser.value = data.session?.user;
    }else if(event == AuthChangeEvent.signedIn){
      currentUser.value = data.session?.user;
    }
  });

  Future<List<Map<String, dynamic>>> searchProducts(String query) async {
    final response = await SupabaseServices.supabaseClient
        .from('product_table') // Replace with your actual table name
        .select('*')
        .ilike('name', '%$query%'); // Case-insensitive search
    return response;
  }
}