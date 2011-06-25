use Math::Trig;

open O, ">xyz.bin";
binmode O;

while(<>) {
    if (/[0-9]/) {
        s/[\r\n]+//;
        @line = split(/\t/);
        $mag = $line[7];
        if($mag>8) {
            next;
        }
        $ra = dms($line[1],$line[2],$line[3])/12*pi;
        $dec = dms($line[4],$line[5],$line[6])/180*pi;
        $cosdec = cos($dec);
        $x = cos($ra) * $cosdec;
        $y = sin($ra) * $cosdec;
        $z = sin($dec);
        print O pack("f>f>f>f>", $x,$y,$z,$mag);
    }
}

sub dms {
   my $x = shift;
   my $d = shift;
   my $m = shift;

   if ($x<0) {
      $sign = -1;
      $x = -$x;
   }
   else {
      $sign = 1;
   }

   return $sign*($x + ($d + $m/60)/60);
}
