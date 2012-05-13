Synth.csd - This file is part of Cobosoda.

Cobosoda is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Cobosoda is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Cobosoda.  If not, see <http://www.gnu.org/licenses/>.

<CsoundSynthesizer>
<CsOptions>

</CsOptions>
<CsInstruments>

sr     = 44100
kr     = 1470
ksmps  = 30    ; 30 frames per second
nchnls = 2


opcode Smoothy, a, ki
	setksmps 1

	kin, iamt xin

	kin port kin, iamt
	aout = kin
	xout aout
endop



instr 1
	iamp  = 5000 * p4
	ifreq = p5
	ipan  = p6

	aenv linseg 0, p3 * 0.1, 1, p3 * 0.9, 0

	a1 oscils iamp, ifreq, 0
	amix = a1 * aenv

	outs amix * sqrt( 1 - ipan ), amix * sqrt( ipan )

endin




instr 9
	chnset 0, "SpringEnergy.0"
	chnset 0, "SpringEnergy.1"
	chnset 0, "SpringEnergy.2"
	chnset 0, "SpringEnergy.3"
	chnset 0, "SpringEnergy.4"
	chnset 0, "SpringEnergy.5"
	chnset 0, "SpringEnergy.6"
	chnset 0, "SpringEnergy.7"
	chnset 0, "SpringEnergy.8"
	chnset 0, "SpringEnergy.9"
	chnset 0, "SpringEnergy.10"
	chnset 0, "SpringEnergy.11"
	chnset 0, "SpringEnergy.12"
	chnset 0, "SpringEnergy.13"
	chnset 0, "SpringEnergy.14"
	chnset 0, "SpringEnergy.15"
	chnset 0, "SpringEnergy.16"
	chnset 0, "SpringEnergy.17"
	chnset 0, "SpringEnergy.18"
	chnset 0, "SpringEnergy.19"
	chnset 0, "SpringEnergy.20"
	chnset 0, "SpringEnergy.21"
	turnoff
endin

; Excite Springs
instr 10
	ispringIndex  = p4

	Sstring sprintf "SpringEnergy.%d", ispringIndex

	kval chnget Sstring

	if (kval >= 1000 ) goto end
	
	kval = kval + 100
	chnset kval, Sstring

	end:
	turnoff

	contiue:
endin



; Excite Springs
instr 11
	ispringIndex  = p4

	Sstring sprintf "SpringEnergy.%d", ispringIndex

	kval chnget Sstring

	if (kval >= 1000 ) goto end
	
	kval = kval + 100 * p5

	chnset kval, Sstring

	end:
	turnoff

	contiue:
endin



; Turn on Springs
instr 20
	; Read in this springs chn info
	; Energy to Amplitude, excited by collisions
	; Spring A() to frequency
	; Drain frequency constant.


	ispringIndex = p4
	iamp  =      1000
	ifreq =      240
	ipan  =      0.5

	Senergy sprintf "SpringEnergy.%d", ispringIndex
	Sstring sprintf "Spring.%d", ispringIndex
	k1 chnget Sstring
	k2 chnget Senergy
	
	afoo Smoothy k1, 0.05
	abar Smoothy k2, 0.01

	a1 oscil abar * 8, ifreq + (ifreq * afoo * 10), 1
;	a1 oscil 1000, ifreq + (ifreq * k1 * 20), 1
		
	outs a1, a1

	k2 = k2 * 0.990
;	k2 = k2 * 0.992
	k2 limit k2, 0, 1000

	chnset k2, Senergy
endin

</CsInstruments>
<CsScore>
f 1 0 8192 10 1  ; Sine Table
f 100 1000       ; Dummy f-table to extend time

i 9 0 1
i 20 0 1000 0
i 20 0 1000 1
i 20 0 1000 2
i 20 0 1000 3
i 20 0 1000 4
i 20 0 1000 5
i 20 0 1000 6
i 20 0 1000 7
i 20 0 1000 8
i 20 0 1000 9
i 20 0 1000 10
i 20 0 1000 11
i 20 0 1000 12
i 20 0 1000 13
i 20 0 1000 14
i 20 0 1000 15
i 20 0 1000 16
i 20 0 1000 17
i 20 0 1000 18
i 20 0 1000 19
i 20 0 1000 20
i 20 0 1000 21




</CsScore>
</CsoundSynthesizer>


