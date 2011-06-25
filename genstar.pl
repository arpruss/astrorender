if (@ARGV != 4 && @ARGV != 7) {
    die "genstar.pl filename res sigma1 r1 [height2 sigma2 r2]";
}

open O, ">$ARGV[0].rgba" or die;
binmode O;

$res =    $ARGV[1];
$sigma1 = $ARGV[2];
$r1  =    $ARGV[3];
if (@ARGV==6) {
   $height2 = $ARGV[4];
   $sigma2 = $ARGV[5];
   $r2     = $ARGV[6];
}
else {
   $height2 = 0;
}

$S1 = $sigma1*$sigma1;
$S2 = $sigma2*$sigma2;

$y1 = exp(-$r1*$r1/$S1);
$scale1 = (1-$height2) / (1-$y1);
$delta1 = 1 - $scale1;

if (defined($r2)) {
    $scale2 = $height2 / (exp(-$r1*$r1/$S2) - exp(-$r2*$r2/$S2));
    $delta2 = $height2 - $scale2 * exp(-$r1*$r1/$S2);
}

$total = 0;
for $x (0..($res-1)) {
  for $y (0..($res-1)) {
     $rr = ($x - ($res-1)/2)*($x - ($res-1)/2) +
          ($y - ($res-1)/2)*($y - ($res-1)/2);
     $h = int(height(sqrt($rr))*255+.5);
     $total += $h;
     print O pack("CCCC", 255,255,255,$h);
  }
}
print "Average weight: ".$total/($res*$res)."\n";
close O;
$out = $ARGV[0];
$specs = $ARGV[1]."_".$ARGV[2]."_".$ARGV[3];
if (defined($r2)) {
    $specs = $specs. "_".$ARGV[4]."_".$ARGV[5]."_".$ARGV[6];
}
$w = int($total/($res*$res)+.5);
$specs = $specs . "_".$w;
$out =~ s/SPECS/$specs/;
system "convert -size ".$res."x".$res." -depth 8 \"".$ARGV[0].".rgba\" \"".$out."\"";
unlink "$ARGV[0].rgba";
print "Generated $out\n";

sub height {
   my $r = shift;

   if ($r < $r1) {
       return exp(-$r*$r/$S1)*$scale1 + $delta1;
   }
   elsif (defined($r2) && $r < $r2) {
       return exp(-$r*$r/$S2)*$scale2 + $delta2;
   }
   else {
       return 0;
   }
}
