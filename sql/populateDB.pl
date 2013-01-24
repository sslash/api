#! /usr/bin/perl
use POSIX qw/strftime/;

use strict;

my @countries = ("Norway", "Sweden", "Denmark", "Australia", "USA", "England",
"Finland", "Germany", "Argentina", "Spain");

my @g = ("Gibson les paul", "Fender stratocaster", "Fender telecaster",
"Music man luke", "Ibanez REM", "Ibanez RG", "Gibson sg",
"Gibson explorer", "Peavey wolfgang", "Gibson flying v");

my @eq = ("Marshall", "Fender", "ENGL", "Mesa", "Orange", "Peavey", "Morgan",
"Randall", "Line 6", "Vox");

my @img =("EddieVanHalen.jpg", "RobertoDeMicheli.jpg", "michael.jpg",
"MikeSpike.jpg", "SteveLukather.jpg", "michaelH.jpg", "Hslash.jpg",
"SteveLukatherH.jpg", "patty ho.jpg", "JohnPetrucci.jpg",
"SteveMorse.jpg", "richie.jpg", "JohnPetrucciH.jpg", "SteveVai.jpg",
"richieH.jpg", "Thor.jpg", "slash.jpg", "MartSpart.jpg", "weegs.JPG",
"MikeSpike.jpg", "YngwieMalmsteen.jpg", "Roberto.jpg" );



my $date = strftime "%Y-%m-%d %H:%M:%S", localtime;

open (MYFILE, '>>populateDB.sql');
open (STUFF, '>>populateStuff.sql')
open (SHREDS, '>>populateShreds.sql');
open (BATTLES, '>>populateBattles.sql');
open (FANS, '>>populateFans.sql');
open(BATTLEREQ, '>>populateBattleRequests.sql');
open (SHREDDERS, '>>populateShredders.sql');


# create 1000 shredders
for (my $i = 0; $i < 1000; $i++) {
    my $rand1 = int(rand(scalar @countries));
    my $rand2 = int(rand(scalar @img));
    my $rand3 = int(rand(100));
    my $rand5 = int(rand(100));

    my $randg = int(rand(scalar @g));
    my $rande = int(rand(scalar @eq));
    
    
    my $rand4 = int(rand(22));
    
    my $name = "Shredder" . $i;
    my $mail = "shredder". $i. "\@slash.com";
    my $description = "Simple test shredder #".$i;
    my $country = $countries[$rand1];
    my $image = $img[$rand2];
    
    
    my $SQL = "INSERT INTO Shredder values (DEFAULT, '$name', '$date', '$mail', '1111', '$description', '$country', DEFAULT, '$image', $rand3, $rand5);\n";
    my $shredder = "(SELECT Id FROM Shredder WHERE username='$name')";
    
    my $guitar = "INSERT INTO GuitarForShredder VALUES ('$randg', $shredder);\n";
    my $equiptment = "INSERT INTO EquiptmentForShredder VALUES ('$rande', $shredder);\n";
    
    print MYFILE "$SQL$guitar$equiptment";
}



my @tagsArr = ("Lick", "Fast", "Technique", "Sweeping", "Tapping", "Cover", "Solo",
"Instruction", "Sound test", "Mind blowing", "Passionate", "British",
"Punk", "Grip", "Chords", "Melody", "Scale", "Show-off", "Jazz", "Fusion",
"Rock", "Metal", "Pop", "Rap", "Funk", "Acoustic", "Chops", "Jam", "Improvisation");



for ( my $i = 0; $i < scalar @tagsArr; $i++ ) {
    my $sql = "INSERT INTO Tag VALUES ( DEFAULT, '$tagsArr[$i]');";
    #print STUFF "$sql\n";
}

my @shr = ("1.mp4", "13.mp4", "17.mp4", "20.mp4", "24.mp4", "26.mp4", "3.mp4",
        "4.mp4", "8.mp4", "sapsapsap2.mp4", "sfsdf.mp4", "ynsrv.mp4", "10.mp4",
        "14.mp4", "18.mp4", "21.mp4", "23shred1.mp4", "27.mp4", "30.mp4",
        "5.mp4", "9.mp4", "9shred7.mp4", "crap.mp4", "dminor.mp4", "f.mp4",
        "g.mp4", "legato.mp4", "lovaabadname.mp4", "11.mp4", "15.mp4",
        "19.mp4", "22.mp4", "24shred5.mp4", "28.mp4", "31.mp4", "6.mp4",
        "sfhd.mp4", "sleep.mp4", "vid.mp4", "12.mp4", "16.mp4", "2.mp4",
        "23.mp4", "23shred2.mp4", "25.mp4", "29.mp4", "32.mp4", "7.mp4",
        "9shred3.mp4", "c.mp4", "dfh.mp4", "edf.mp4", "fgdf.mp4", "h.mp4",
        "livinprayer.mp4", "s.mp4");

my @thumbs = ("23shred1.jpg", "9shred7.jpg", "crap.jpg", "dminor.jpg",
        "f.jpg", "g.jpg", "legato.jpg", "lovaabadname.jpg", "test2.jpg",
        "24shred5.jpg", "sfhd.jpg", "sleep.jpg", "vid.jpg", "23shred2.jpg",
        "9shred3.jpg", "c.jpg", "dfh.jpg", "edf.jpg", "fgdf.jpg", "h.jpg",
        "livinprayer.jpg", "s.jpg", "sfsdf.jpg", "test.jpg", "ynsrv.jpg");

# Create 10000 shreds SELECT Id FROM Shredder ORDER BY RANDOM() LIMIT 1;
for (my $i = 0; $i < 50000; $i++) {
    my $randTmb = int(rand(scalar @thumbs)); 
	 my $ran2 = int(rand(scalar @shr));
     my $tagsRan1 = int(rand(scalar @tagsArr));
     my $tagsRan2 = int(rand(scalar @tagsArr));
    if ( $tagsRan1 == $tagsRan2) {
        $tagsRan2 = int(rand(scalar @tagsArr));
    }
    my $ranrate = int(rand(1000));
    my $ranrate2 = int(rand(10000));
    my $commentText1 = "Lorem ipsum lol cat mode".$i;
    my $commentText2 = "Lorem ipsum lol cat mode".$i + 1;
    
    my $img = $thumbs[$randTmb];
    my $description = "Simple test shred #" . $i; 
    my $owner = "SELECT Id FROM Shredder ORDER BY RANDOM() LIMIT 1";
    my $timeCreated = "DEFAULT";
    my $shredType = "normal";
    my $videoPath = $shr[$ran2];
    my $videoThumbnail = $img;
    my $sql = "INSERT INTO Shred VALUES(DEFAULT, '$description', ($owner),".
        "$timeCreated, '$videoPath', '$shredType', '$img');\n";

    my $tagsSQL = "INSERT INTO TagsForShred VALUES(".
        "(SELECT currval('Shred_Id_seq')), ".
        "(SELECT Id FROM Tag WHERE Label='$tagsArr[$tagsRan1]'));\n";
    my $tagsSQL2 = "INSERT INTO TagsForShred VALUES(".
        "(SELECT currval('Shred_Id_seq')), ".
        "(SELECT Id FROM Tag WHERE Label='$tagsArr[$tagsRan2]'));\n";

    my $commentowner = "SELECT Id FROM Shredder ORDER BY RANDOM() LIMIT 1";
    my $commentSQL = "INSERT INTO CommentForShred VALUES (".
    "DEFAULT, '$commentText1', ($commentowner), DEFAULT, (SELECT currval('Shred_Id_seq')) );\n ";
    my $comment2SQL = "INSERT INTO CommentForShred VALUES (".
        "DEFAULT, '$commentText2', ($commentowner), DEFAULT, (SELECT currval('Shred_Id_seq')) );\n ";

    my $rating = "INSERT INTO Rating VALUES (".
        "(SELECT currval('Shred_Id_seq')), $ranrate2, $ranrate);\n";
    print MYFILE $sql.$tagsSQL.$tagsSQL2.$commentSQL.$comment2SQL.$rating;
}

# create battles
my $size = 1000; # size of shredders! Add updated value here!
for ( my $i = 0; $i < 10000; $i++) {
    my $ran1 = int(rand($size));
    my $ran2 = int(rand($size));
    my $ran3 = int(rand(41));
    my $ransh = int(rand(scalar @shr));
    my $randTmb = int(rand(scalar @thumbs)); 
    my $img = $thumbs[$randTmb];
    my $sh =  $shr[$ransh];
    my $videoThumbnail = $img;

    my $battleSQL = "INSERT INTO Battle VALUES(".
        "DEFAULT,".
        "(SELECT Id FROM Shredder ORDER BY RANDOM() LIMIT 1),".
        "(SELECT Id FROM Shredder ORDER BY RANDOM() LIMIT 1),".
        "DEFAULT,".
        "4,".
        "'accepted',".
        "DEFAULT);\n";

    my $shredSQL = "INSERT INTO Shred VALUES (".
        "DEFAULT,".
        "'',".
        "(SELECT Id FROM Shredder ORDER BY RANDOM() LIMIT 1),". # this is illegal, but I just need to have a shred owner
        "DEFAULT,".
        "'$sh',".
        "'battle',".
        "'$img');\n";

    my $shredForBattleSQL = "INSERT INTO ShredForBattle VALUES(".
        "(SELECT currval('Shred_Id_seq')),".
        "(SELECT currval('Battle_Id_seq')),".
        "DEFAULT);\n";

    print MYFILE $battleSQL.$shredSQL.$shredForBattleSQL;
}


for ( my $i = 0; $i < 1000; $i++) {

    # Max 10 fanees
    my $faneeNum = int(rand(10));

    for ( my $y = 0; $y < $faneeNum; $y++) {
        my $fanSQL = "INSERT INTO Fan VALUES(".
            "(SELECT Id FROM Shredder ORDER BY RANDOM() LIMIT 1), ".
            "(SELECT Id FROM Shredder ORDER BY RANDOM() LIMIT 1), ".
            "DEFAULT);\n";
        print MYFILE $fanSQL;
    }
}



# create battle requests!
my $size = 1000; # size of shredders! Add updated value here!
for ( my $i = 0; $i < 1000; $i++) {
    my $ran1 = int(rand($size));
    my $ran2 = int(rand($size));
    my $ran3 = int(rand(41));
    my $ransh = int(rand(scalar @shr));
    my $randTmb = int(rand(scalar @thumbs)); 
     my $img = $thumbs[$randTmb];
    my $sh =  $shr[$ransh];
    
    
    my $battleSQL = "INSERT INTO Battle VALUES(".
    "DEFAULT,".
    "(SELECT Id FROM Shredder ORDER BY RANDOM() LIMIT 1),".
    "(SELECT Id FROM Shredder ORDER BY RANDOM() LIMIT 1),".
    "DEFAULT,".
    "4,".
    "'awaiting',".
    "DEFAULT);\n";
    
    my $shredSQL = "INSERT INTO Shred VALUES (".
    "DEFAULT,".
    "'',".
    "(SELECT Id FROM Shredder ORDER BY RANDOM() LIMIT 1),". # this is illegal, but I just need to have a shred owner
    "DEFAULT,".
    "'$sh',".
    "'battle',".
    "'$img');\n";
    
    my $shredForBattleSQL = "INSERT INTO ShredForBattle VALUES(".
    "(SELECT currval('Shred_Id_seq')),".
    "(SELECT currval('Battle_Id_seq')),".
    "DEFAULT);\n";
    
    print MYFILE $battleSQL.$shredSQL.$shredForBattleSQL;
}



close (MYFILE);
close (SHREDDERS);
close(SHREDS);
close(BATTLES);
close(FANS);